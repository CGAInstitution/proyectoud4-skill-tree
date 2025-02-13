package madstodolist.service;

import madstodolist.dto.NotaData;
import madstodolist.dto.TareaData;
import madstodolist.model.Nota;
import madstodolist.model.Tarea;
import madstodolist.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@Service
public class NotaService {


    private final NotaRepository notaRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }


    @Transactional(readOnly = true)
    public NotaData findById(Long idNota) {
        Nota nota = notaRepository.findById(idNota).orElseThrow(()->new RuntimeException("Nota no encontrada"));

        return modelMapper.map(nota, NotaData.class);
    }

    public boolean actualizarTituloNota(Long idNota, String titulo) {
        Optional<Nota> nota = notaRepository.findById(idNota);
        if (nota.isPresent()) {
            Nota notaActual = nota.get();
            notaActual.setTitulo(titulo);
            notaRepository.save(notaActual);
            return true;
        }
        return false;
    }

    public boolean actualizarDescripcionNota(Long idNota, String descripcion) {
        Optional<Nota> nota = notaRepository.findById(idNota);
        if (nota.isPresent()) {
            Nota notaActual = nota.get();
            notaActual.setDescripcion(descripcion);
            notaRepository.save(notaActual);
            return true;
        }
        return false;
    }
}
