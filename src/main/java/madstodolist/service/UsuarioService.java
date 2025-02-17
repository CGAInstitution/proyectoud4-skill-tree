package madstodolist.service;

import madstodolist.dto.UsuarioData;
import madstodolist.model.Escritorio;
import madstodolist.model.Nota;
import madstodolist.model.Preferencia;
import madstodolist.model.Usuario;
import madstodolist.repository.EscritorioRepository;
import madstodolist.repository.PreferenciaRepository;
import madstodolist.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UsuarioService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    @Autowired
    private PreferenciaRepository preferenciaRepository;

    public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD}

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EscritorioRepository escritorioRepository;

    @Transactional(readOnly = true)
    public LoginStatus login(String eMail, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(eMail);
        String hashedPassword = hashPassword(password);
        if (!usuario.isPresent()) {
            return LoginStatus.USER_NOT_FOUND;
        } else if (usuario.get().getContraseña().compareTo(hashedPassword) != 0) {
            return LoginStatus.ERROR_PASSWORD;
        } else {
            return LoginStatus.LOGIN_OK;
        }
    }

    // Se añade un usuario en la aplicación.
    // El email y password del usuario deben ser distinto de null
    // El email no debe estar registrado en la base de datos
    @Transactional
    public UsuarioData registrar(UsuarioData usuario) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioBD.isPresent())
            throw new UsuarioServiceException("El usuario " + usuario.getEmail() + " ya está registrado");
        else if (usuario.getEmail() == null)
            throw new UsuarioServiceException("El usuario no tiene email");
        else if (usuario.getContraseña() == null)
            throw new UsuarioServiceException("El usuario no tiene password");
        else {
            Usuario usuarioNuevo = modelMapper.map(usuario, Usuario.class);
            usuarioNuevo.setContraseña(hashPassword(usuario.getContraseña()));

            //Guardamos al usuario con un escritorio vacío por defecto
            Escritorio escritorio = new Escritorio();
            escritorio.setNombre("Escritorio 1");
            usuarioNuevo.addEscritorio(escritorio);

            usuarioNuevo = usuarioRepository.save(usuarioNuevo);
            escritorio.setIdUsuario(usuarioNuevo);
            escritorioRepository.save(escritorio);
            return modelMapper.map(usuarioNuevo, UsuarioData.class);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioData findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) return null;
        else {
            return modelMapper.map(usuario, UsuarioData.class);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioData findById(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return null;
        else {
            return modelMapper.map(usuario, UsuarioData.class);
        }
    }

    @Transactional(readOnly = true)
    public Escritorio obtenerPrimerEscritorio(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioServiceException("Usuario no encontrado"));

        return escritorioRepository.findFirstByIdUsuarioOrderByIdAsc(usuario)
                .orElseThrow(() -> new UsuarioServiceException("No se encontró un escritorio para el usuario"));
    }



    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Agregar un cero si es necesario
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash de la contraseña", e);
        }
    }

    public boolean actualizarNombre(Long idUsuario, String nombre) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isPresent()) {
            Usuario usuarioActual = usuario.get();
            usuarioActual.setNombre(nombre);
            usuarioRepository.save(usuarioActual);
            return true;
        }
        return false;
    }

    public boolean actualizarApellido(Long idUsuario, String apellido) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isPresent()) {
            Usuario usuarioActual = usuario.get();
            usuarioActual.setApellidos(apellido);
            usuarioRepository.save(usuarioActual);
            return true;
        }
        return false;
    }

    public boolean actualizarEmail(Long idUsuario, String email) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isPresent()) {
            Usuario usuarioActual = usuario.get();
            usuarioActual.setEmail(email);
            usuarioRepository.save(usuarioActual);
            return true;
        }
        return false;
    }

    public boolean actualizarContrasenia(Long idUsuario, String contrasenia) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isPresent()) {
            Usuario usuarioActual = usuario.get();

            usuarioActual.setContraseña(hashPassword(contrasenia));
            usuarioRepository.save(usuarioActual);
            return true;
        }
        return false;
    }

}
