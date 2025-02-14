package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.controller.exception.TareaNotFoundException;
import madstodolist.dto.TareaData;
import madstodolist.dto.UsuarioData;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TareaController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    ManagerUserSession managerUserSession;

    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }

    @GetMapping("/usuarios/{id}/tareas")
    public String a(@PathVariable(value="id") Long idUsuario,
                                 @ModelAttribute TareaData tareaData, Model model) {

        comprobarUsuarioLogeado(idUsuario);

        UsuarioData usuario = usuarioService.findById(idUsuario);
        model.addAttribute("usuario", usuario);
        return "test";
    }
}

