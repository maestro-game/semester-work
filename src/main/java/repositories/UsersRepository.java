package repositories;

import models.User;

import java.sql.Date;

public interface UsersRepository extends CrudRepository<User, String> {
    void updateField(String id, String field, String data);
}
