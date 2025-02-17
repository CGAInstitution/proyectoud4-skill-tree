package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.SettingsForm;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

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

    @PostMapping("/user/settings/savesettings")
    public String guardarSettings(@Valid @ModelAttribute SettingsForm settingsForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            UsuarioData userData = usuarioService.findById(managerUserSession.usuarioLogeado());
            model.addAttribute("usuario", userData);
            return "formSettings";
        }
        usuarioService.actualizarNombre(managerUserSession.usuarioLogeado(), settingsForm.getNombre());
        usuarioService.actualizarApellido(managerUserSession.usuarioLogeado(), settingsForm.getApellidos());
        usuarioService.actualizarEmail(managerUserSession.usuarioLogeado(), settingsForm.getEmail());
        if (settingsForm.getContrasenia() != null) {
            usuarioService.actualizarContrasenia(managerUserSession.usuarioLogeado(), settingsForm.getContrasenia());

        }
        redirectAttributes.addFlashAttribute("show", true);
        return "redirect:/user/settings";
    }
}
