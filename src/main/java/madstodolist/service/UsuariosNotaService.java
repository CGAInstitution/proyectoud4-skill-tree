package madstodolist.service;

import madstodolist.model.UsuariosNota;
import madstodolist.repository.UsuariosNotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuariosNotaService {

    private final UsuariosNotaRepository usuariosNotaRepository;

    @Autowired
    public UsuariosNotaService(UsuariosNotaRepository usuariosNotaRepository) {
        this.usuariosNotaRepository = usuariosNotaRepository;
    }

    public void save(UsuariosNota usuariosNota) {
        usuariosNotaRepository.save(usuariosNota);
    }
}
