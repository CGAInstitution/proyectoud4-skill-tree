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
        Nota nota = notaRepository.findById(idNota).orElse(null);
        if (nota == null) {
         //Todo devuelve un error
         return null;
        }
        else return modelMapper.map(nota, NotaData.class);
    }
}
