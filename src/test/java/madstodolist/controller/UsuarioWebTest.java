package madstodolist.controller;

import madstodolist.Application;
import madstodolist.authentication.ManagerUserSession;
import madstodolist.builders.EscritorioBuilder;
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

import static org.hamcrest.Matchers.*;
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
    @Autowired
    private ModelMapper modelMapper;

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

        Escritorio escritorio1 = new EscritorioBuilder()
                .withNombre("Jonh's Escritorio").withUsuario(modelMapper.map(usuario1, Usuario.class))
                .build();
        Escritorio escritorio2 = new EscritorioBuilder()
                .withNombre("Jane's Escritorio").withUsuario(modelMapper.map(usuario2, Usuario.class))
                .build();

        Map<String, Object> results = new HashMap<>();
        results.put("John", usuario1.getId());
        results.put("Jane", usuario2.getId());
        results.put("John's Escritorio", escritorio1.getId());
        results.put("Jane's Escritorio", escritorio2.getId());

        return results;
    }

    @Test
    public void mostrarEscritorioTest() throws Exception {
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

    @Test
    public void redireccionWhenNoSessionTest() throws Exception {
        //GIVEN
        //Una sesión vacía
        when(managerUserSession.usuarioLogeado()).thenReturn(null);

        //WHEN, THEN
        //Una peticicón a /escritorio debería redirigirnos a /login
        this.mockMvc.perform(get("/escritorio"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void redireccionWhenSessionTest() throws Exception {
        //GIVEN
        //Una sesión activa
        Long idUsuario = (Long) prepareBD().get("Jane");
        when(managerUserSession.usuarioLogeado()).thenReturn(idUsuario);

        //WHEN, THEN
        //Una petición a /login debería redirigirnos a /escritorio
        this.mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/escritorio"));
    }

    @Test
    public void deleteEscritorioTest() throws Exception {
        //GIVEN
        //Un usuario registrado con varios escritorios creados
        Map<String, Object> bd = prepareBD();
        Long idUsuario = (Long) bd.get("Jane");
        Long idEscritorio = (Long) bd.get("Jane's Escritorio");

        //Moqueamos inicio de sesión del usuario
        when(managerUserSession.usuarioLogeado()).thenReturn(idUsuario);
        when(managerUserSession.currentEscritorio()).thenReturn(
                usuarioService.obtenerPrimerEscritorio(idUsuario).getId()
        );

        //WHEN, THEN
        //Borramos uno de los escritorios del usuario debería de desaparecer de la vista
        mockMvc.perform(delete("/escritorio/" + idEscritorio + "/eliminar"))
                .andExpect(content().string(not(containsString("Jane's Escritorio"))));
    }
}