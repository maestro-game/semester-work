package repositories;

import models.Category;
import models.FollowCat;

import java.util.List;

public interface FollowCatsRepository extends CrudRepository<FollowCat, FollowCat> {
    List<Category> findByUserId(String id);
}
