package madstodolist.repository;

import madstodolist.builders.NotaBuilder;
import madstodolist.model.Categoria;
import madstodolist.model.Escritorio;
import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class NotaTest {
    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EscritorioRepository escritorioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Usuario usuarioTest = new Usuario();
    private Escritorio escritorioTest = new Escritorio();
    private Categoria categoriaTest = new Categoria();

    //Tests de modelo de notas en memoria, sin conexión a la BD

    private void prepare() {
        usuarioTest.setEmail("johndoe@gmail.com");
        usuarioTest.setNombre("John");
        usuarioTest.setApellidos("Doe");
        usuarioTest.setContraseña("12345678");
        usuarioRepository.save(usuarioTest);

        escritorioTest.setNombre("Escritorio de prueba");
        escritorioTest.setIdUsuario(usuarioTest);
        escritorioRepository.save(escritorioTest);

        categoriaTest.setNombre("Prueba");
        categoriaTest.setIdUsuario(usuarioTest);
        categoriaTest.setColor("ffd6a5");
        categoriaRepository.save(categoriaTest);
    }

    @Test
    public void crearNota() throws Exception {
        //GIVEN una nota creada
        Nota nota = new Nota();

        // WHEN
        // actualizamos sus propiedades usando los setters,
        nota.setTitulo("Prueba");
        nota.setDescripcion("Test de creación de nota");

        // THEN
        // los valores actualizados quedan guardados en la nota y se
        // pueden recuperar con los getters.
        assertThat(nota.getTitulo()).isEqualTo("Prueba");
        assertThat(nota.getDescripcion()).isEqualTo("Test de creación de nota");
    }

    @Test
    public void comprobarIgualdadesPorId() throws Exception {
        //GIVEN notas con los mismos atributos pero diferentes ids
        Nota nota1 = new NotaBuilder().withId(1L).build();
        Nota nota2 = new NotaBuilder().withId(2L).build();

        //THEN las notas son diferentes porque no comparten id
        assertThat(nota1).isNotEqualTo(nota2);

        //GIVEN notas con distintos atributos pero el mismo id
        Nota nota3 = new NotaBuilder().withId(3L)
                .withTitulo("Hola").withDescripcion("Hola!").withColor("ffd6a5")
                .build();
        Nota nota4 = new NotaBuilder().withId(3L)
                .withTitulo("Adiós").withDescripcion("Adiós!").withColor("fae890")
                .build();

        //THEN ambas notas son iguales porque comparten id
        assertThat(nota3).isEqualTo(nota4);
    }

    @Test
    @Transactional
    public void crearNotaBaseDeDatos() throws Exception {
        //GIVEN una nota recién creada sin id junto con las referencias necesarias
        prepare();
        Nota nota = new NotaBuilder()
                .withUsuario(usuarioTest).withEscritorio(escritorioTest).withCategoria(categoriaTest)
                .build();

        //WHEN la guardamos en la base de datos
        notaRepository.save(nota);

        //THEN se le asigna un id a la nota persistida
        assertThat(nota.getId()).isNotNull();
    }

    @Test
    @Transactional
    public void buscarNotaEnBaseDeDatos() throws Exception {
        //GIVEN una nota en la BD
        prepare();
        Nota nota = new NotaBuilder()
                .withUsuario(usuarioTest).withEscritorio(escritorioTest).withCategoria(categoriaTest)
                .build();
        notaRepository.save(nota);

        //WHEN la recuperamos desde la BD con su id
        Nota notaBD = notaRepository.findById(nota.getId()).orElse(null);

        //THEN comprobamos que las propiedades de la nota recuperada se corresponden con las de la nota en la BD
        //y que son iguales al tener el mismo id
        assertThat(notaBD.getTitulo()).isEqualTo(nota.getTitulo());
        assertThat(notaBD.getDescripcion()).isEqualTo(nota.getDescripcion());
        assertThat(notaBD.getColor()).isEqualTo(nota.getColor());
        assertThat(notaBD).isEqualTo(nota);
    }
}
