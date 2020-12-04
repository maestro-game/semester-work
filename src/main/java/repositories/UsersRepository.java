package repositories;

import models.User;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<User, String> {
    void updateField(String id, String field, String data);
    Optional<User> findByIdWithPassword(String id);
    List<User> searchById(String id, int offset, int limit);
    RowMapper<User> getRowMapper();
}
