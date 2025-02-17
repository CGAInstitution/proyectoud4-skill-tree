package madstodolist.repository;

import madstodolist.model.Nota;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class NotaTest {
    @Autowired
    private NotaRepository notaRepository;

    //Tests de modelo de notas en memoria, sin conexión a la BD

    @Test
    public void crearNota() throws Exception {
        //IVEN una nota creada
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
}
