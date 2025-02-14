package madstodolist.service;


import madstodolist.model.Escritorio;
import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import madstodolist.repository.EscritorioRepository;
import madstodolist.repository.NotaRepository;
import madstodolist.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class  EscritorioService {

    private final NotaRepository notaRepository;
    private final EscritorioRepository escritorioRepository;

    @Autowired
    public EscritorioService(NotaRepository notaRepository, EscritorioRepository escritorioRepository) {
        this.notaRepository = notaRepository;
        this.escritorioRepository = escritorioRepository;
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


}
