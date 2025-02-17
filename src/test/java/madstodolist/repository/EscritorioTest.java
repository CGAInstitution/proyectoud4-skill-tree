package madstodolist.repository;

import madstodolist.builders.EscritorioBuilder;
import madstodolist.model.Escritorio;
import madstodolist.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EscritorioTest {
    @Autowired
    private EscritorioRepository escritorioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioTest = new Usuario();

    //Tests de modelo de escritorios en memoria, sin conexión a la BD

    private void prepare() {
        usuarioTest.setEmail("johndoe@gmail.com");
        usuarioTest.setNombre("John");
        usuarioTest.setApellidos("Doe");
        usuarioTest.setContraseña("12345678");
        usuarioRepository.save(usuarioTest);
    }

    @Test
    public void crearEscritorioTest() {
        //GIVEN un escritorio recién creado
        Escritorio escritorio = new Escritorio();

        //WHEN
        //establecemos sus propiedades con setters
        escritorio.setNombre("Escritorio prueba");
        escritorio.setIdUsuario(usuarioTest);

        //THEN
        //los valores actualizados se guardan en el escritorio y se pueden recuperar con los getters
        assertThat(escritorio.getNombre()).isEqualTo("Escritorio prueba");
        assertThat(escritorio.getIdUsuario()).isEqualTo(usuarioTest);
    }

    @Test
    public void comprobarIgualdadesPorId() throws Exception {
        //GIVEN escritorios con los mismos atributos pero diferentes ids
        Escritorio escritorio1 = new EscritorioBuilder().withId(1L).build();
        Escritorio escritorio2 = new EscritorioBuilder().withId(2L).build();

        //THEN los escritorios son diferentes porque no comparten id
        assertThat(escritorio1).isNotEqualTo(escritorio2);

        //GIVEN escritorios con distintos atributos pero el mismo id
        Escritorio escritorio3 = new EscritorioBuilder().withId(3L)
                .withNombre("Hola")
                .build();
        Escritorio escritorio4 = new EscritorioBuilder().withId(3L)
                .withNombre("Adiós")
                .build();

        //THEN ambas notas son iguales porque comparten id
        assertThat(escritorio3).isEqualTo(escritorio4);
    }

    @Test
    @Transactional
    public void crearEscritorioBaseDeDatos() throws Exception {
        //GIVEN un escritorio recién creado sin id junto con las referencias necesarias
        prepare();
        Escritorio escritorio = new EscritorioBuilder()
                .withUsuario(usuarioTest)
                .build();

        //WHEN lo guardamos en la base de datos
        escritorioRepository.save(escritorio);

        //THEN se le asigna un id a la nota persistida
        assertThat(escritorio.getId()).isNotNull();
    }

    @Test
    @Transactional
    public void buscarEscritorioEnBaseDeDatos() throws Exception {
        //GIVEN un escritorio en la BD
        prepare();
        Escritorio escritorio = new EscritorioBuilder()
                .withUsuario(usuarioTest)
                .build();
        escritorioRepository.save(escritorio);

        //WHEN lo recuperamos desde la BD con su id
        Escritorio escritorioBD = escritorioRepository.findById(escritorio.getId()).orElse(null);

        //THEN comprobamos que las propiedades del escritorio recuperado se corresponden con las del escritorio en la BD
        //y que son iguales al tener el mismo id
        assertThat(escritorioBD.getNombre()).isEqualTo(escritorio.getNombre());
        assertThat(escritorioBD.getIdUsuario()).isEqualTo(escritorio.getIdUsuario());
        assertThat(escritorioBD).isEqualTo(escritorio);
    }
}
