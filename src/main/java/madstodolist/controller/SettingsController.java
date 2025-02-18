package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.UsuarioData;
import madstodolist.model.Usuario;
import madstodolist.service.PreferenciaService;
import madstodolist.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {
    private final ManagerUserSession managerUserSession;
    private final UsuarioService usuarioService;
    private final PreferenciaService preferenciaService;

    private final ModelMapper modelMapper;

    @Autowired
    public SettingsController(ManagerUserSession managerUserSession, UsuarioService usuarioService, PreferenciaService preferenciaService
            , ModelMapper modelMapper) {
        this.managerUserSession = managerUserSession;
        this.usuarioService = usuarioService;
        this.preferenciaService = preferenciaService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/user/settings")
    public String mostrarSettings(Model model) {
        UsuarioData userData = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", userData);
        return "formSettings";
    }

    @PostMapping("/user/settings/savesettings")
    public String guardarSettings(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("email") String email,
            @RequestParam(value = "contraseña", required = false) String contrasenia,
            @RequestParam(value = "confirmarContrasenia", required = false) String confirmarContrasenia,
            RedirectAttributes redirectAttributes) {

        if (contrasenia != null && !contrasenia.isEmpty()) {
            if (!contrasenia.equals(confirmarContrasenia)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/user/settings";
            }
        }

        Long idUsuario = managerUserSession.usuarioLogeado();
        Usuario usuario =modelMapper.map( usuarioService.findById(idUsuario), Usuario.class);

        // Actualizar los datos básicos
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setEmail(email);

        if (contrasenia != null && !contrasenia.isEmpty()) {
            usuario.setContraseña(contrasenia);
        }

        usuarioService.save(usuario);

        // Redirigir a la vista con un mensaje de éxito
        redirectAttributes.addFlashAttribute("show", true);
        return "redirect:/user/settings";
    }

}
