package madstodolist.repository;


import madstodolist.model.Escritorio;
import madstodolist.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EscritorioRepository extends CrudRepository<Escritorio, Long> {
    Optional<Escritorio> findFirstByIdUsuarioOrderByIdAsc(Usuario usuario);
    List<Escritorio> findAllByIdUsuarioOrderByIdDesc(Usuario usuario);

    Usuario id(Long id);
}

