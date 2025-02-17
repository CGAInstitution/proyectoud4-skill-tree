package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.EscritorioCreateData;
import madstodolist.dto.LoginData;
import madstodolist.dto.UsuarioData;
import madstodolist.model.Escritorio;
import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import madstodolist.service.EscritorioService;
import madstodolist.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Controller
public class EscritorioController {

    private final EscritorioService escritorioService;
    private final ManagerUserSession managerUserSession;
    private final UsuarioService usuarioService;
    private final ModelMapper modelMapper;

    @Autowired
    public EscritorioController(EscritorioService escritorioService, ManagerUserSession managerUserSession,
                                UsuarioService usuarioService, ModelMapper modelMapper) {
        this.escritorioService = escritorioService;
        this.managerUserSession = managerUserSession;
        this.usuarioService = usuarioService;
        this.modelMapper = modelMapper;
    }

    /*@GetMapping("/usuarios/{idUsuario}/escritorios/{idEscritorio}")
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
    }*/

    @GetMapping("/escritorio")
    public String mostrarEscritorio(Model model) {
        if (managerUserSession.usuarioLogeado() == null) {
            return "redirect:/login";
        }

        UsuarioData userData = usuarioService.findById(managerUserSession.usuarioLogeado());
        Usuario currentlyLoggedUser = modelMapper.map(userData, Usuario.class);

        List<Escritorio> escritorios = escritorioService.obtenerEscritoriosPorUsuario(currentlyLoggedUser);
        model.addAttribute("escritorios", escritorios);

        List<Nota> notas = escritorioService.obtenerNotasPorEscritorio(managerUserSession.currentEscritorio());
        Set<Nota> notasCompartidas = currentlyLoggedUser.getNotasCompartidas();

        if (notas != null && !notas.isEmpty()) {
            System.out.println("Número de notas encontradas: " + notas.size());
            model.addAttribute("notas", notas);
        } else {
            System.out.println("No hay notas en este escritorio.");
            model.addAttribute("notas", new ArrayList<>());
        }

        if (notasCompartidas != null && !notasCompartidas.isEmpty()) {
            model.addAttribute("notasCompartidas", notasCompartidas);
        } else {
            System.out.println("No hay notas en este escritorio.");
            model.addAttribute("notasCompartidas", new ArrayList<>());
        }

        return "escritorio";
    }

    @PostMapping("/notas/{idNota}/actualizar-posicion")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizarPosicionNota(
            @PathVariable Long idNota,
            @RequestBody Map<String, Object> requestData) {

        Integer posicionX = (Integer) requestData.get("posicionX");
        Integer posicionY = (Integer) requestData.get("posicionY");

        boolean success = escritorioService.actualizarPosicionNota(idNota, posicionX, posicionY);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/notas/{idNota}/eliminar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarNota(@PathVariable Long idNota) {
        System.out.println(idNota);
        boolean success = escritorioService.eliminarNota(idNota);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/escritorio/change")
    public ResponseEntity<Map<String, Object>> changeEscritorio(@RequestBody Map<String, Long> requestData) {
        Map<String, Object> response = new HashMap<>();
        managerUserSession.setCurrentEscritorio(requestData.get("idEscritorio"));

        if (Objects.equals(managerUserSession.currentEscritorio(), requestData.get("idEscritorio"))) {
            response.put("success", true);
        } else {
            response.put("success", false);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/escritorio/create")
    public String crearEscritorio(Model model) {
        model.addAttribute("escritorioData", new EscritorioCreateData());
        return "escritorioForm";
    }

    @PostMapping("/escritorio/submit")
    public String submitEscritorio(@ModelAttribute("escritorioData") @Valid EscritorioCreateData escritorioData,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                   HttpSession session) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/escritorio/create";
        }

        Escritorio escritorio = new Escritorio();
        escritorio.setNombre(escritorioData.getNombre());
        escritorio.setIdUsuario(modelMapper.map(usuarioService.findById(managerUserSession.usuarioLogeado()), Usuario.class));

        escritorioService.save(escritorio);

        return "close";
    }

    @PostMapping("/notas/nueva")
    public String crearNota(@RequestParam(required = false) Integer posicionX,
                            @RequestParam(required = false) Integer posicionY) {
        Long idUsuario = managerUserSession.usuarioLogeado();
        Long idEscritorio = managerUserSession.currentEscritorio();

        System.out.println("valor x");
        System.out.printf(String.valueOf(posicionX));

        if (posicionX == null || posicionY == null) {
            posicionX = 500;
            posicionY = 500;
        }

        Nota nuevaNota = escritorioService.crearNuevaNota(idUsuario, idEscritorio, posicionX, posicionY);

        if (nuevaNota != null) {
            return "redirect:/notas/" + nuevaNota.getId();
        } else {
            return "error";
        }
    }
    @DeleteMapping("/escritorio/{idEscritorio}/eliminar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarEscritorio(@PathVariable Long idEscritorio) {
        boolean success = escritorioService.eliminarEscritorio(idEscritorio);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        return ResponseEntity.ok(response);
    }
}
