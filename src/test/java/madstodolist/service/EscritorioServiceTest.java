package madstodolist.service;

import madstodolist.builders.EscritorioBuilder;
import madstodolist.builders.NotaBuilder;
import madstodolist.model.Categoria;
import madstodolist.model.Escritorio;
import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import madstodolist.repository.CategoriaRepository;
import madstodolist.repository.EscritorioRepository;
import madstodolist.repository.NotaRepository;
import madstodolist.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EscritorioServiceTest {
    @Autowired
    private EscritorioService escritorioService;

    @Autowired
    private EscritorioRepository escritorioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotaService notaService;

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private NotaRepository notaRepository;

    private Map<String, Object> prepareBD() {
        Map<String, Object> data = new HashMap<>();

        Usuario usuario1 = new Usuario();
        usuario1.setEmail("johndoe@gmail.com");
        usuario1.setNombre("John");
        usuario1.setApellidos("Doe");
        usuario1.setContraseña("123");
        Usuario usuario2 = new Usuario();
        usuario2.setEmail("janedoe@gmail.com");
        usuario2.setNombre("Jane");
        usuario2.setApellidos("Doe");
        usuario2.setContraseña("abc");
        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);

        Escritorio escritorio1 = new EscritorioBuilder()
                .withNombre("John's Escritorio").withUsuario(usuario1)
                .build();
        Escritorio escritorio2 = new EscritorioBuilder()
                .withNombre("Jane's Escritorio").withUsuario(usuario2)
                .build();
        escritorioRepository.save(escritorio1);
        escritorioRepository.save(escritorio2);

        Categoria categoria1 = new Categoria();
        categoria1.setNombre("John's");
        categoria1.setIdUsuario(usuario1);
        categoria1.setColor("ffd6a5");
        Categoria categoria2 = new Categoria();
        categoria2.setNombre("Jane's");
        categoria2.setIdUsuario(usuario2);
        categoria2.setColor("ffd6a5");
        categoriaRepository.save(categoria1);
        categoriaRepository.save(categoria2);

        Nota nota1 = new NotaBuilder()
                .withTitulo("John's Note").withCategoria(categoria1).withUsuario(usuario1).withEscritorio(escritorio1)
                .build();
        Nota nota2 = new NotaBuilder()
                .withTitulo("Jane's Note").withCategoria(categoria2).withUsuario(usuario2).withEscritorio(escritorio2)
                .build();
        notaService.save(nota1);
        notaService.save(nota2);

        data.put("John", usuario1);
        data.put("Jane", usuario2);
        data.put("John's Escritorio", escritorio1);
        data.put("Jane's Escritorio", escritorio2);
        data.put("John's Categoria", categoria1);
        data.put("Jane's Categoria", categoria2);
        data.put("John's Nota", nota1);
        data.put("Jane's Nota", nota2);
        return data;
    }

    @Test
    public void obtenerNotasPorEscritorioTest() throws Exception {
        //GIVEN un escritorio en la BD con una nota relacionada
        Map<String, Object> data = prepareBD();
        Escritorio escritorio = (Escritorio) data.get("John's Escritorio");

        //WHEN obtenemos un listado de sus notas
        List<Nota> notas = escritorioService.obtenerNotasPorEscritorio(escritorio.getId());

        //THEN notas no debería estar vacío y debería contener la nota correspondiente
        assertThat(notas.isEmpty()).isFalse();
        assertThat(notas.contains((Nota) data.get("John's Nota"))).isTrue();
    }

    @Test
    public void actualizarPosicionNotaTest() throws Exception {
        //GIVEN un escritorio en la BD con una nota relacionada
        Map<String, Object> data = prepareBD();
        Escritorio escritorio = (Escritorio) data.get("John's Escritorio");

        //WHEN actualizamos la posición de una de sus notas
        List<Nota> notas = escritorioService.obtenerNotasPorEscritorio(escritorio.getId());
        escritorioService.actualizarPosicionNota(notas.get(0).getId(), 10, 20);

        //THEN las coordenadas de la nota deberian haberse actualizado
        assertThat(notaRepository.findById(notas.get(0).getId()).get().getPosicionX()).isEqualTo(10);
        assertThat(notaRepository.findById(notas.get(0).getId()).get().getPosicionY()).isEqualTo(20);
    }

    @Test
    public void eliminarNotaTest() throws Exception {
        //GIVEN un escritorio en la BD con una nota relacionada
        Map<String, Object> data = prepareBD();
        Escritorio escritorio = (Escritorio) data.get("John's Escritorio");

        //WHEN borramos su nota
        List<Nota> notas = escritorioService.obtenerNotasPorEscritorio(escritorio.getId());
        escritorioService.eliminarNota(notas.get(0).getId());

        //THEN el escritorio no debería tener la nota borrada y en este caso no debería tener ninguna nota
        assertThat(escritorio.getNotas().contains((Nota) data.get("John's Nota"))).isFalse();
        assertThat(escritorio.getNotas().isEmpty()).isTrue();
    }

    @Test
    public void obtenerEscritorioPorUsuarioTest() throws Exception {
        //GIVEN dos usuarios en la BD con sus respectivos escritorios
        Map<String, Object> data = prepareBD();
        Usuario usuario1 = (Usuario) data.get("John");
        Usuario usuario2 = (Usuario) data.get("Jane");
        Escritorio escritorio1 = (Escritorio) data.get("John's Escritorio");
        Escritorio escritorio2 = (Escritorio) data.get("Jane's Escritorio");

        //WHEN buscamos escritorios por sus usuarios
        List<Escritorio> escritoriosJohn = escritorioService.obtenerEscritoriosPorUsuario(usuario1);
        List<Escritorio> escritoriosJane = escritorioService.obtenerEscritoriosPorUsuario(usuario2);

        //THEN cada lista debería contener el escritorio esperado
        assertThat(escritoriosJohn.contains(escritorio1)).isTrue();
        assertThat(escritoriosJane.contains(escritorio2)).isTrue();
        assertThat(escritoriosJohn.contains(escritorio2)).isFalse();
        assertThat(escritoriosJane.contains(escritorio1)).isFalse();
    }

    @Test
    @Transactional
    public void saveEscritorioTest() throws Exception {
        //GIVEN un escritorio recién creado
        Map<String, Object> data = prepareBD();
        Usuario usuario1 = (Usuario) data.get("John");
        Escritorio escritorio = new EscritorioBuilder()
                .withNombre("Prueba creación").withUsuario(usuario1)
                .build();

        //WHEN lo persistimos en la BD
        escritorioService.save(escritorio);
        Escritorio escritorioBD = escritorioRepository.findById(escritorio.getId()).get();

        //THEN el escritorio debería tener un id asignado y lo podríamos obtener de la BD sin problemas
        assertThat(escritorio.getId()).isNotNull();
        assertThat(escritorioBD).isNotNull();
        assertThat(escritorioBD.getNombre()).isEqualTo("Prueba creación");
        assertThat(escritorioBD.getIdUsuario()).isEqualTo(usuario1);
    }

    /*@Test
    public void crearNotaTest() throws Exception {
        //GIVEN un escritoio en la BD
        Map<String, Object> data = prepareBD();
        Escritorio escritorio = (Escritorio) data.get("Jane's Escritorio");
        Usuario usuario = (Usuario) data.get("Jane");

        //WHEN creamos una nueva nota en el escritorio
        Nota nota = escritorioService.crearNuevaNota(usuario.getId(), escritorio.getId(), 20, 10);

        //THEN la nota debería hebrse persistido en la BD y el escritorio debería contener una nota más
        assertThat(nota.getId()).isNotNull();
        assertThat(nota.getPosicionX()).isEqualTo(20);
        assertThat(nota.getPosicionY()).isEqualTo(10);
    }*/

    @Test
    @Transactional
    public void eliminarEscriorioTest() throws Exception {
        //GIVEN un escritorio en la BD
        Map<String, Object> data = prepareBD();
        Usuario usuario = (Usuario) data.get("Jane");
        Escritorio escritorio1 = (Escritorio) data.get("Jane's Escritorio");

        //WHEN borramos su escritoio
        Map<String, Object> response = new HashMap<>();
        escritorioService.eliminarEscritorio(escritorio1.getId(), response);

        //THEN el escritorio no debería haberse borrado porque es el único que el usuario tiene
        Escritorio escritorio1BD = escritorioRepository.findById(escritorio1.getId()).get();
        assertThat(escritorio1BD).isNotNull();
        assertThat(response.get("error")).isEqualTo("Debes tener al menos un escritorio.");

        //GIVEN un usuario en la BD con múltiple escritorios
        usuario.addEscritorio(escritorio1BD);
        Escritorio escritorio2 = new EscritorioBuilder().withUsuario(usuario).withNombre("Prueba").build();
        escritorioService.save(escritorio2);
        usuario.addEscritorio(escritorio2);

        //WHEN borramos un escritorio
        escritorioService.eliminarEscritorio(escritorio2.getId(), response);

        //THEN el escritorio debería haberse borrado
        Optional<Escritorio> escritorio2BD = escritorioRepository.findById(escritorio2.getId());
        assertThat(escritorio2BD.isPresent()).isFalse();
    }
}
