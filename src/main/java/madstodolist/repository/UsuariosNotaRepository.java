package madstodolist.repository;

import madstodolist.model.UsuariosNota;
import madstodolist.model.UsuariosNotaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosNotaRepository extends JpaRepository<UsuariosNota, UsuariosNotaId> {
}

