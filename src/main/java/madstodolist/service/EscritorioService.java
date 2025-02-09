package madstodolist.service;


import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import madstodolist.repository.NotaRepository;
import madstodolist.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscritorioService {

    private final NotaRepository notaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public EscritorioService(NotaRepository notaRepository, UsuarioRepository usuarioRepository) {
        this.notaRepository = notaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Nota> obtenerNotasPorUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario != null) {
            return notaRepository.findByIdCreador(usuario);
        } else {
            return null;
        }
    }
}