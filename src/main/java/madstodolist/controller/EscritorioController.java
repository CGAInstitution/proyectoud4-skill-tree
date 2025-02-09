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

import java.util.List;

@Controller
public class EscritorioController {

    private final EscritorioService escritorioService;

    @Autowired
    public EscritorioController(EscritorioService escritorioService) {
        this.escritorioService = escritorioService;
    }

    @GetMapping("/usuarios/{id}/escritorio")
    public String mostrarEscritorio(@PathVariable(value="id") Long idUsuario, Model model) {
        List<Nota> notas = escritorioService.obtenerNotasPorUsuario(idUsuario);

        if (notas != null) {
            System.out.println("Número de notas encontradas: " + notas.size()); // Agregar un log de la cantidad de notas
            model.addAttribute("notas", notas);
        } else {
            System.out.println("Usuario no encontrado.");
        }

        return "escritorio"; // Thymeleaf usará "escritorio.html"
    }
}
