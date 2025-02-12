package madstodolist.controller;

import madstodolist.dto.NotaData;
import madstodolist.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class NoteEditingController {

    private final NotaService notaService;
    @Autowired
    public NoteEditingController(NotaService notaService) {this.notaService = notaService;}

    @GetMapping("/usuarios/{idUsuario}/escritorios/{idEscritorio}/notas/{idNota}")
    public String mostrarNotaEditable(@PathVariable Long idUsuario, @PathVariable Long idEscritorio, @PathVariable Long idNota, Model model){
        NotaData nota = notaService.findById(idNota);
        model.addAttribute("nota", nota);
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

}
