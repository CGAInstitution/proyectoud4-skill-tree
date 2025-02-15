package madstodolist.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class NoteEditingController {

    @Autowired
    private ModelMapper modelMapper;

    private final NotaService notaService;
    private final UsuarioService usuarioService;
    private final UsuariosNotaService usuariosNotaService;

    @Autowired
    public NoteEditingController(NotaService notaService,UsuarioService usuarioService, UsuariosNotaService usuariosNotaService) {
        this.notaService = notaService;
        this.usuarioService = usuarioService;
        this.usuariosNotaService = usuariosNotaService;
    }

    @GetMapping("/notas/{idNota}")
    public String mostrarNotaEditable(@PathVariable Long idNota, Model model){
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

}
