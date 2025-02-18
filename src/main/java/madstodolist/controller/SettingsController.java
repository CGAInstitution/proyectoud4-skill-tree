package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.UsuarioData;
import madstodolist.model.Preferencia;
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
        Long idUsuario = managerUserSession.usuarioLogeado();

        UsuarioData userData = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", userData);

        Preferencia preferencia = preferenciaService.findById(idUsuario);
        model.addAttribute("preferencia", preferencia);


        return "formSettings";
    }

    @PostMapping("/user/settings/savesettings")
    public String guardarSettings(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("email") String email,
            @RequestParam(value = "contraseña", required = false) String contrasenia,
            @RequestParam(value = "confirmarContrasenia", required = false) String confirmarContrasenia,
            @RequestParam("passwordActual") String passwordActual,
            @RequestParam("modo") boolean modo,
            @RequestParam("tamano_fuente") int tamanoFuente,
            @RequestParam("idioma") String idioma,
            RedirectAttributes redirectAttributes) {

        if (contrasenia != null && !contrasenia.isEmpty()) {
            if (!contrasenia.equals(confirmarContrasenia)) {
                redirectAttributes.addFlashAttribute("errorContrasenas", "Las contraseñas no coinciden");
                return "redirect:/user/settings";
            }
        }

        Long idUsuario = managerUserSession.usuarioLogeado();
        Usuario usuario = modelMapper.map(usuarioService.findById(idUsuario), Usuario.class);

        String hashedPasswordActual = UsuarioService.hashPassword(passwordActual);
        if (!usuario.getContraseña().equals(hashedPasswordActual)) {
            redirectAttributes.addFlashAttribute("errorPasswordActual", "La contraseña actual es incorrecta");
            return "redirect:/user/settings";
        }

        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setEmail(email);

        if (contrasenia != null && !contrasenia.isEmpty()) {
            usuario.setContraseña(UsuarioService.hashPassword(contrasenia));
        }

        usuarioService.save(usuario);

        preferenciaService.updatePreferencias(idUsuario, modo, tamanoFuente, idioma);

        redirectAttributes.addFlashAttribute("show", true);
        return "redirect:/user/settings";
    }

}
