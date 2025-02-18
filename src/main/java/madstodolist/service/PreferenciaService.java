package madstodolist.service;

import madstodolist.model.Nota;
import madstodolist.model.Preferencia;
import madstodolist.repository.PreferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PreferenciaService {
    private final PreferenciaRepository preferenciaRepository;

    @Autowired
    public PreferenciaService(PreferenciaRepository preferenciaRepository) {
        this.preferenciaRepository = preferenciaRepository;
    }





    public void crearPreferencia(Preferencia preferencia) {
        preferenciaRepository.save(preferencia);
    }


    public Preferencia findById(Long idUsuario) {
        Preferencia preferencia = preferenciaRepository.findById(idUsuario).orElseThrow(()->new RuntimeException("Preferencia no encontrada"));
        return preferencia;

    }

    public void updatePreferencias(Long idUsuario, boolean modo, int tamanoFuente, String idioma) {
        Preferencia preferencia = findById(idUsuario);
        preferencia.setModoOscuro(modo);
        preferencia.setTamañoFuente((byte) tamanoFuente);
        preferencia.setIdioma(idioma);
        System.out.println(preferencia);
        preferenciaRepository.save(preferencia);
    }

}
