package madstodolist.controller;

import madstodolist.dto.NotaData;
import madstodolist.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
