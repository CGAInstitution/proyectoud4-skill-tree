package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.NotaData;
import madstodolist.dto.UsuarioData;
import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import madstodolist.model.UsuariosNota;
import madstodolist.model.UsuariosNotaId;
import madstodolist.service.NotaService;
import madstodolist.service.UsuarioService;
import madstodolist.service.UsuariosNotaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
public class NoteEditingController {

    @Autowired
    private ModelMapper modelMapper;

    private final NotaService notaService;
    private final UsuarioService usuarioService;
    private final UsuariosNotaService usuariosNotaService;
    private final ManagerUserSession managerUserSession;

    @Autowired
    public NoteEditingController(NotaService notaService,UsuarioService usuarioService,
                                 UsuariosNotaService usuariosNotaService, ManagerUserSession managerUserSession) {
        this.notaService = notaService;
        this.usuarioService = usuarioService;
        this.usuariosNotaService = usuariosNotaService;
        this.managerUserSession = managerUserSession;
    }

    @GetMapping("/notas/{idNota}")
    public String mostrarNotaEditable(@PathVariable Long idNota, Model model){
        if (!checkUserAccess(managerUserSession.usuarioLogeado(), idNota)) {
            return "redirect:/escritorio";
        }

        NotaData nota = notaService.findById(idNota);
        model.addAttribute("nota", nota);
        model.addAttribute("idNota", idNota);
        return "notaEditable";
    }

    @PostMapping("/notas/{idNota}/actualizar-titulo")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> actualizarTitulo(@PathVariable Long idNota, @RequestBody Map<String, Object> requestData){
        String titulo = (String) requestData.get("titulo");

        boolean success=notaService.actualizarTituloNota(idNota, titulo);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/notas/{idNota}/actualizar-descripcion")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> actualizarDescripcion(@PathVariable Long idNota, @RequestBody Map<String, Object> requestData){
        String descripcion = (String) requestData.get("descripcion");

        boolean success=notaService.actualizarDescripcionNota(idNota, descripcion);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/notas/{idNota}/actualizar-color")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizarColor(@PathVariable Long idNota, @RequestBody Map<String, Object> requestData) {
        String color = (String) requestData.get("color");

        boolean success = notaService.actualizarColorNota(idNota, color);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/notas/compartir")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> compartirNota(@RequestBody Map<String, Object> requestData) {
        Long idNota = Long.valueOf(requestData.get("idNota").toString());
        String correo = (String) requestData.get("correo");

        // Buscar usuario por correo
        UsuarioData usuarioData = usuarioService.findByEmail(correo);

        if (usuarioData == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Convertir UsuarioData a Usuario usando ModelMapper
        Usuario usuario = modelMapper.map(usuarioData, Usuario.class);

        // Crear la relación en la tabla usuarios_nota
        UsuariosNota usuariosNota = new UsuariosNota();
        UsuariosNotaId usuariosNotaId = new UsuariosNotaId();
        usuariosNotaId.setIdNota(idNota);
        usuariosNotaId.setIdUsuario(usuario.getId()); // Asumimos que `usuario.getId()` devuelve el ID del usuario
        usuariosNota.setId(usuariosNotaId);
        usuariosNota.setIdUsuario(usuario);
        Nota nota = notaService.findNotaById(idNota);

        nota.setId(idNota);
        usuariosNota.setIdNota(nota);

        // Guardar la relación
        usuariosNotaService.save(usuariosNota);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("notas/{idNota}/descargar")
    public ResponseEntity<byte[]> sownloadNota(@PathVariable Long idNota) {
        Nota nota = notaService.findNotaById(idNota);
        String titulo = nota.getTitulo();
        String descripcion = nota.getDescripcion();

        String markdownContent = "# " + titulo + "\n" + descripcion;

        byte[] contentBytes = markdownContent.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=nota.md");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/markdown; charset=UTF-8");

        return new ResponseEntity<>(contentBytes, headers, HttpStatus.OK);
    }

    private boolean checkUserAccess(long userId, long notaId) {
        boolean hasAccess = false;

        Usuario usuario = modelMapper.map(usuarioService.findById(userId), Usuario.class);
        Nota nota = notaService.findNotaById(notaId);

        if (nota.getIdCreador().getId().equals(usuario.getId())) {
            hasAccess = true;
            return hasAccess;
        }

        if (nota.getUsuarios().contains(usuario)) {
            hasAccess = true;
            return hasAccess;
        }

        return hasAccess;
    }


}
