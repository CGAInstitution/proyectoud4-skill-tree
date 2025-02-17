package madstodolist.service;

import madstodolist.builders.NotaBuilder;
import madstodolist.dto.NotaData;
import madstodolist.model.Categoria;
import madstodolist.model.Escritorio;
import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import madstodolist.repository.CategoriaRepository;
import madstodolist.repository.EscritorioRepository;
import madstodolist.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class NotaServiceTest {
    @Autowired
    private NotaService notaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EscritorioRepository escritorioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

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

        Escritorio escritorio1 = new Escritorio();
        escritorio1.setNombre("John's Escritorio");
        escritorio1.setIdUsuario(usuario1);
        Escritorio escritorio2 = new Escritorio();
        escritorio2.setNombre("Jane's Escritorio");
        escritorio2.setIdUsuario(usuario2);
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

        data.put("John", usuario1);
        data.put("Jane", usuario2);
        data.put("John's Escritorio", escritorio1);
        data.put("Jane's Escritorio", escritorio2);
        data.put("John's Categoria", categoria1);
        data.put("Jane's Categoria", categoria2);
        return data;
    }

    Long addNotaToBD() {
        Map<String, Object> bd = prepareBD();
        Nota nota = new NotaBuilder()
                .withUsuario((Usuario) bd.get("John"))
                .withEscritorio((Escritorio) bd.get("John's Escritorio"))
                .withCategoria((Categoria) bd.get("John's Categoria"))
                .build();
        Nota nuevaNota = notaService.save(nota);
        return nuevaNota.getId();
    }

    @Test
    public void servicioFindByIdTest() throws Exception {
        //GIVEN una nota registrada en la BD
        long id = addNotaToBD();

        //WHEN recuperamos una nota desde la BD con su id
        Nota nota = notaService.findNotaById(id);

        //THEN comprobamos que la nota obtenida es efectivamente la nota de la BD
        assertThat(nota.getId()).isEqualTo(id);
        assertThat(nota.getTitulo()).isEqualTo("Nota Test");
        assertThat(nota.getDescripcion()).isEqualTo("Esto es una nota de prueba");
    }

    @Test
    public void servicioChangePropertiesTest() throws Exception {
        //GIVEN una nota registrada en la BD
        long id = addNotaToBD();

        //WHEN la recuperamos de la BD, cambiamos sus propiedades y la persistimos
        Nota nota = notaService.findNotaById(id);
        nota.setTitulo("Test cambio");
        nota.setDescripcion("Esta nota ha sido cambiada");
        notaService.save(nota);

        assertThat(notaService.findNotaById(id).getTitulo()).isEqualTo("Test cambio");
        assertThat(notaService.findNotaById(id).getDescripcion()).isEqualTo("Esta nota ha sido cambiada");
    }
}
