package madstodolist.repository;

import madstodolist.model.Categoria;
import madstodolist.model.Escritorio;
import org.springframework.data.repository.CrudRepository;

public interface CategoriaRepository extends CrudRepository<Categoria, Long> {
}
