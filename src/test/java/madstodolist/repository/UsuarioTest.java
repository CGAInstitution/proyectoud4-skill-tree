package madstodolist.repository;

import madstodolist.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class UsuarioTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //
    // Tests modelo Usuario en memoria, sin la conexión con la BD
    //

    @Test
    public void crearUsuario() throws Exception {

        // GIVEN
        // Creado un nuevo usuario,
        Usuario usuario = new Usuario();

        // WHEN
        // actualizamos sus propiedades usando los setters,
        usuario.setEmail("johndoe@gmail.com");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setContraseña("12345678");

        // THEN
        // los valores actualizados quedan guardados en el usuario y se
        // pueden recuperar con los getters.
        assertThat(usuario.getEmail()).isEqualTo("johndoe@gmail.com");
        assertThat(usuario.getNombre()).isEqualTo("John");
        assertThat(usuario.getApellidos()).isEqualTo("Doe");
        assertThat(usuario.getContraseña()).isEqualTo("12345678");
    }

    @Test
    public void comprobarIgualdadUsuariosSinId() {
        // GIVEN
        // Creados tres usuarios sin identificador, y dos de ellas con
        // el mismo e-mail
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("johndoe@gmail.com");
        Usuario usuario2 = new Usuario();
        usuario2.setEmail("janedoe@gmail.com");
        Usuario usuario3 = new Usuario();
        usuario3.setEmail("johndoe@gmail.com");

        // THEN
        // son iguales (Equal) los que tienen el mismo e-mail.
        assertThat(usuario1).isNotEqualTo(usuario2);
        assertThat(usuario1).isEqualTo(usuario3);
    }


    @Test
    public void comprobarIgualdadUsuariosConId() {
        // GIVEN
        // Creadas tres usuarios con distintos e-mails y dos de ellos
        // con el mismo identificador,
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("johndoe@gmail.com");
        usuario1.setId(1L);
        Usuario usuario2 = new Usuario();
        usuario2.setEmail("janedoe@gmail.com");
        usuario2.setId(2L);
        Usuario usuario3 = new Usuario();
        usuario3.setEmail("johnnydoe@gmail.com");
        usuario3.setId(1L);

        // THEN
        // son iguales (Equal) los usuarios que tienen el mismo identificador.
        assertThat(usuario1).isEqualTo(usuario3);
        assertThat(usuario1).isNotEqualTo(usuario2);
    }

    //
    // Tests UsuarioRepository.
    // El código que trabaja con repositorios debe
    // estar en un entorno transactional, para que todas las peticiones
    // estén en la misma conexión a la base de datos, las entidades estén
    // conectadas y sea posible acceder a colecciones LAZY.
    //
    @Test
    @Transactional
    public void crearUsuarioBaseDatos() throws ParseException {
        // GIVEN
        // Un usuario nuevo creado sin identificador
        Usuario usuario = new Usuario();
        usuario.setEmail("johndoe@gmail.com");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setContraseña("12345678");

        // WHEN
        // se guarda en la base de datos
        usuarioRepository.save(usuario);

        // THEN
        // se actualiza el identificador del usuario,
        assertThat(usuario.getId()).isNotNull();

        // y con ese identificador se recupera de la base de datos el usuario con
        // los valores correctos de las propiedades.
        Usuario usuarioBD = usuarioRepository.findById(usuario.getId()).orElse(null);
        assertThat(usuarioBD.getEmail()).isEqualTo("johndoe@gmail.com");
        assertThat(usuarioBD.getNombre()).isEqualTo("John");
        assertThat(usuarioBD.getApellidos()).isEqualTo("Doe");
        assertThat(usuarioBD.getContraseña()).isEqualTo("12345678");
    }

    @Test
    @Transactional
    public void buscarUsuarioEnBaseDatos() {
        // GIVEN
        // Un usuario en la BD
        Usuario usuario = new Usuario();
        usuario.setEmail("johndoe@gmail.com");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setContraseña("12345678");
        usuarioRepository.save(usuario);
        Long usuarioId = usuario.getId();

        // WHEN
        // se recupera de la base de datos un usuario por su identificador,
        Usuario usuarioBD = usuarioRepository.findById(usuarioId).orElse(null);

        // THEN
        // se obtiene el usuario correcto y se recuperan sus propiedades.
        assertThat(usuarioBD).isNotNull();
        assertThat(usuarioBD.getId()).isEqualTo(usuarioId);
        assertThat(usuarioBD.getNombre()).isEqualTo("John");
    }

    @Test
    @Transactional
    public void buscarUsuarioPorEmail() {
        // GIVEN
        // Un usuario en la BD
        Usuario usuario = new Usuario();
        usuario.setEmail("johndoe@gmail.com");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setContraseña("12345678");
        usuarioRepository.save(usuario);

        // WHEN
        // buscamos al usuario por su correo electrónico,
        Usuario usuarioBD = usuarioRepository.findByEmail("johndoe@gmail.com").orElse(null);

        // THEN
        // se obtiene el usuario correcto.
        assertThat(usuarioBD.getNombre()).isEqualTo("John");
    }
}