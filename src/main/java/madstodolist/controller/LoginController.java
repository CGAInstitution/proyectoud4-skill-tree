package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.LoginData;
import madstodolist.dto.RegistroData;
import madstodolist.dto.UsuarioData;
import madstodolist.model.Escritorio;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model, HttpSession session) {
        if (managerUserSession.usuarioLogeado() != null) {
            System.out.println("Hay sesión abierta");
        }
        model.addAttribute("loginData", new LoginData());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute("loginData") @Valid LoginData loginData,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes,
                              HttpSession session) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/login";
        }

        // Llamada al servicio para comprobar si el login es correcto
        UsuarioService.LoginStatus loginStatus = usuarioService.login(loginData.getEmail(), loginData.getPassword());

        if (loginStatus == UsuarioService.LoginStatus.USER_NOT_FOUND) {
            redirectAttributes.addFlashAttribute("errors",
                    new ObjectError("login", "El usuario no existe"));
            return "redirect:/login";
        }

        if (loginStatus == UsuarioService.LoginStatus.ERROR_PASSWORD) {
            redirectAttributes.addFlashAttribute("errors",
                    new ObjectError("login", "Contraseña incorrecta"));
            return "redirect:/login";
        }

        if (loginStatus == UsuarioService.LoginStatus.LOGIN_OK) {
            UsuarioData usuario = usuarioService.findByEmail(loginData.getEmail());
            managerUserSession.logearUsuario(usuario.getId());

            Escritorio primerEscritorio = usuarioService.obtenerPrimerEscritorio(usuario.getId());
            return "redirect:/usuarios/" + usuario.getId() + "/escritorios/" + primerEscritorio.getId();

        }

        return "login";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("registroData", new RegistroData());
        return "registro";
    }

   @PostMapping("/registro")
   public String registroSubmit(@ModelAttribute("registroData") @Valid RegistroData registroData,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {

       if (bindingResult.hasErrors()) {
           redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
           return "redirect:/registro";
       }

        if (usuarioService.findByEmail(registroData.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("errors",
                    new ObjectError("registro", "El email ya existe"));
            return "redirect:/registro";
        }

        UsuarioData usuario = new UsuarioData();
        usuario.setEmail(registroData.getEmail());
        usuario.setContraseña(registroData.getPassword());
        usuario.setNombre(registroData.getNombre());
        usuario.setApellidos(registroData.getApellidos());

        usuarioService.registrar(usuario);
       System.out.println("Usuario registrado con éxito");
        return "redirect:/login";
   }

   @GetMapping("/logout")
   public String logout(HttpSession session) {
        managerUserSession.logout();
        return "redirect:/login";
   }
}
