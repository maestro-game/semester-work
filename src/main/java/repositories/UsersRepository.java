package repositories;

import models.User;

public interface UsersRepository extends CrudRepository<User, String> {
    boolean isExist(String id, byte[] hashPassword);
}
