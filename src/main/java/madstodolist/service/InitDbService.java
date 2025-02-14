package madstodolist.service;

import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.repository.TareaRepository;
import madstodolist.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
// Se ejecuta solo si el perfil activo es 'dev'
@Profile("dev")
public class InitDbService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TareaRepository tareaRepository;

    // Se ejecuta tras crear el contexto de la aplicación
    // para inicializar la base de datos
}
