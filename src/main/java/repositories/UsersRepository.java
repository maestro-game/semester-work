package repositories;

import models.User;

import java.util.List;

public interface UsersRepository extends CrudRepository<User, String> {
    void updateField(String id, String field, String data);
    List<User> searchById(String id, int offset, int limit);
    RowMapper<User> getRowMapper();
}
