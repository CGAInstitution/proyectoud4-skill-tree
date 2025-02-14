package madstodolist.repository;

import madstodolist.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String s);

    Optional<Usuario> findById(Long usuarioId);
}
