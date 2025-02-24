package madstodolist.service;


import madstodolist.model.Categoria;
import madstodolist.model.Escritorio;
import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import madstodolist.repository.CategoriaRepository;
import madstodolist.repository.EscritorioRepository;
import madstodolist.repository.NotaRepository;
import madstodolist.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class  EscritorioService {

    private final NotaRepository notaRepository;
    private final EscritorioRepository escritorioRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;


    @Autowired
    public EscritorioService(NotaRepository notaRepository, EscritorioRepository escritorioRepository,
                             UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository) {
        this.notaRepository = notaRepository;
        this.escritorioRepository = escritorioRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Nota> obtenerNotasPorEscritorio(Long idEscritorio) {
        Escritorio escritorio = escritorioRepository.findById(idEscritorio)
                .orElseThrow(() -> new RuntimeException("Escritorio no encontrado"));

        return notaRepository.findByIdEscritorio(escritorio);
    }
    public boolean actualizarPosicionNota(Long idNota, Integer posicionX, Integer posicionY) {
        Optional<Nota> optionalNota = notaRepository.findById(idNota);
        if (optionalNota.isPresent()) {
            Nota nota = optionalNota.get();
            nota.setPosicionX(posicionX);
            nota.setPosicionY(posicionY);
            notaRepository.save(nota);
            return true;
        }
        return false;
    }
    public boolean eliminarNota(Long idNota) {
        Optional<Nota> optionalNota = notaRepository.findById(idNota);
        if (optionalNota.isPresent()) {
            notaRepository.deleteById(idNota);
            return true;
        }
        return false;
    }

    public List<Escritorio> obtenerEscritoriosPorUsuario(Usuario usuario) {
        return escritorioRepository.findAllByIdUsuarioOrderByIdDesc(usuario);
    }
    public Escritorio save(Escritorio escritorio) {
        return escritorioRepository.save(escritorio);
    }

    public Nota crearNuevaNota(Long idUsuario, Long idEscritorio, Integer posicionX, Integer posicionY) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Escritorio escritorio = escritorioRepository.findById(idEscritorio)
                .orElseThrow(() -> new RuntimeException("Escritorio no encontrado"));

        Nota nuevaNota = new Nota();
        nuevaNota.setTitulo("Nueva Nota");
        nuevaNota.setDescripcion("Vacia");
        nuevaNota.setColor("ffd6a5");
        nuevaNota.setPosicionX(posicionX);
        nuevaNota.setPosicionY(posicionY);
        nuevaNota.setIdCreador(usuario);
        nuevaNota.setIdEscritorio(escritorio);

        notaRepository.save(nuevaNota);

        return nuevaNota;
    }
    public boolean eliminarEscritorio(Long idEscritorio, Map<String, Object> response) {
        Optional<Escritorio> optionalEscritorio = escritorioRepository.findById(idEscritorio);
        if (optionalEscritorio.isPresent()) {
            Escritorio escritorio = optionalEscritorio.get();
            // Verificar la cantidad de escritorios del usuario
            int cantidadEscritorios = escritorioRepository.countByIdUsuario(escritorio.getIdUsuario());
            if (cantidadEscritorios <= 1) {
                response.put("error", "Debes tener al menos un escritorio.");
                return false;
            }
            escritorioRepository.deleteById(idEscritorio);
            return true;
        }
        response.put("error", "Escritorio no encontrado.");
        return false;
    }

}
