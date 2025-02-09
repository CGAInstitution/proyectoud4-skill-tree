package madstodolist.controller;

import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import madstodolist.repository.NotaRepository;
import madstodolist.repository.UsuarioRepository;
import madstodolist.service.EscritorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EscritorioController {

    private final EscritorioService escritorioService;

    @Autowired
    public EscritorioController(EscritorioService escritorioService) {
        this.escritorioService = escritorioService;
    }

    @GetMapping("/usuarios/{idUsuario}/escritorios/{idEscritorio}")
    public String mostrarEscritorio(@PathVariable Long idUsuario, @PathVariable Long idEscritorio, Model model) {
        //ahora no se usa el idUsuario, pero habria que mantenerlo para en un futuro revisar que no ponga un escritorio de otra gente
        List<Nota> notas = escritorioService.obtenerNotasPorEscritorio(idEscritorio);

        if (notas != null && !notas.isEmpty()) {
            System.out.println("Número de notas encontradas: " + notas.size());
            model.addAttribute("notas", notas);
        } else {
            System.out.println("No hay notas en este escritorio.");
            model.addAttribute("notas", new ArrayList<>());
        }

        return "escritorio"; // Thymeleaf usará "escritorio.html"
    }
}
