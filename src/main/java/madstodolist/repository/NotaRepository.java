package madstodolist.repository;

import madstodolist.model.Escritorio;
import madstodolist.model.Nota;
import madstodolist.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface NotaRepository extends CrudRepository<Nota, Long> {
    List<Nota> findByIdCreador(@NotNull Usuario idCreador);
    List<Nota> findByIdEscritorio(@NotNull Escritorio idEscritorio);

}

