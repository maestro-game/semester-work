package repositories;

import models.Category;
import models.FollowUser;
import models.User;

import java.util.List;

public interface FollowUsersRepository extends CrudRepository<FollowUser, FollowUser>{
    List<User> findByUserId(String id);
}
