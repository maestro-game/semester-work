package repositories;

import java.sql.SQLException;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    Optional<T> findById(ID id);
    void save(T entity);
    void update(T entity);
    void deleteById(ID id);
}
