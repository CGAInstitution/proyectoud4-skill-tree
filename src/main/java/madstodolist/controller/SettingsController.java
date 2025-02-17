package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {
    private final ManagerUserSession managerUserSession;
    private final UsuarioService usuarioService;


    @Autowired
    public SettingsController(ManagerUserSession managerUserSession, UsuarioService usuarioService) {
        this.managerUserSession = managerUserSession;
        this.usuarioService = usuarioService;

    }

    @GetMapping("/user/settings")
    public String mostrarSettings(Model model) {
        UsuarioData userData = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", userData);
        return "formSettings";
    }
}
