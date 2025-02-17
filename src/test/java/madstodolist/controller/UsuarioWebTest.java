package madstodolist.controller;

import madstodolist.Application;
import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.LoginData;
import madstodolist.dto.UsuarioData;
import madstodolist.model.Escritorio;
import madstodolist.model.Usuario;
import madstodolist.repository.EscritorioRepository;
import madstodolist.service.EscritorioService;
import madstodolist.service.NotaService;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import javax.naming.Binding;
import javax.persistence.metamodel.Bindable;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql")
public class UsuarioWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EscritorioService escritorioService;

    @Autowired
    private EscritorioRepository escritorioRepository;

    @MockBean
    private ManagerUserSession managerUserSession;

    @MockBean
    private BindingResult bindingResult;

    Map<String, Object> prepareBD() {
        UsuarioData usuario1 = new UsuarioData();
        usuario1.setEmail("johndoe@gmail.com");
        usuario1.setContraseña("123");
        usuario1.setNombre("John");
        usuario1.setApellidos("Doe");
        usuario1 = usuarioService.registrar(usuario1);

        UsuarioData usuario2 = new UsuarioData();
        usuario2.setEmail("janedoe@gmail.com");
        usuario2.setContraseña("abc");
        usuario2.setNombre("Jane");
        usuario2.setApellidos("Doe");
        usuario2 = usuarioService.registrar(usuario2);

        Map<String, Object> results = new HashMap<>();
        results.put("John", usuario1.getId());
        results.put("Jane", usuario2.getId());

        return results;
    }

    @Test
    public void mostrarEscritorio() throws Exception {
        //GIVEN
        //Usuario recién registrado con un escritorio por defecto con nombre: Escritorio1
        Long idUsuario = (Long) prepareBD().get("John");

        //Moqueamos inicio de sesión del usuario recién registrado
        when(managerUserSession.usuarioLogeado()).thenReturn(idUsuario);
        when(managerUserSession.currentEscritorio()).thenReturn(
                usuarioService.obtenerPrimerEscritorio(idUsuario).getId()
        );

        //WHEN, THEN
        //Realizamos la petición get a /escritorio que tendrá en el menu desplegable una tarjeta para Escritorio 1
        this.mockMvc.perform(get("/escritorio"))
                .andExpect((content().string(allOf(
                        containsString("Escritorio 1")
                ))));
    }
}
