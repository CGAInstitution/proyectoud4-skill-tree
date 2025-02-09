package madstodolist.controller;

import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import madstodolist.repository.NotaRepository;
import madstodolist.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class EscritorioController {

    private final NotaRepository notaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository; // Aquí se inyecta el repository

    public EscritorioController(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    @GetMapping("/usuarios/{id}/escritorio")
    public String mostrarEscritorio(@PathVariable(value="id") Long idUsuario, Model model) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario != null) {
            List<Nota> notas = notaRepository.findByIdCreador(usuario);
            System.out.println("Número de notas encontradas: " + notas.size()); // Agregar un log de la cantidad de notas

            model.addAttribute("notas", notas);
        } else {
            System.out.println("Usuario no encontrado.");
        }

        return "escritorio"; // Thymeleaf usará "escritorio.html"
    }
}
