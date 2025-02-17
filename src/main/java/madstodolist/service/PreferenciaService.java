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
    public boolean actualizarModo(Long idPreferencia, boolean modo) {
        Optional<Preferencia> preferencia = preferenciaRepository.findById(idPreferencia);
        if (preferencia.isPresent()) {
            Preferencia preferenciaActual = preferencia.get();
            preferenciaActual.setModoOscuro(modo);
            preferenciaRepository.save(preferenciaActual);
            return true;
        }
        return false;
    }
    public boolean actualizarTamanioFuente(Long idPreferencia, int tamanio) {
        Optional<Preferencia> preferencia = preferenciaRepository.findById(idPreferencia);
        if (preferencia.isPresent()) {
            Preferencia preferenciaActual = preferencia.get();
            preferenciaActual.setTamañoFuente((byte)tamanio);
            preferenciaRepository.save(preferenciaActual);
            return true;
        }
        return false;
    }
    public boolean actualizarIdioma(Long idPreferencia, String idioma) {
        Optional<Preferencia> preferencia = preferenciaRepository.findById(idPreferencia);
        if (preferencia.isPresent()) {
            Preferencia preferenciaActual = preferencia.get();
            preferenciaActual.setIdioma(idioma);
            preferenciaRepository.save(preferenciaActual);
            return true;
        }
        return false;
    }

    public Preferencia findById(Long idUsuario) {
        Preferencia preferencia = preferenciaRepository.findById(idUsuario).orElseThrow(()->new RuntimeException("Preferencia no encontrada"));
        return preferencia;
    }
}
