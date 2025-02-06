package madstodolist.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DragAndDropController {


    @GetMapping("/nota")
    public String greeting(Model model) {
        model.addAttribute("texto", "nota desde controller"); // Aquí defines el texto que se mostrará en el post-it
        return "dragAndDrop";  // Este es el nombre del archivo dragAndDrop.html sin la extensión
    }
}
