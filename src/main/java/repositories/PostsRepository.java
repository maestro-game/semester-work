package repositories;

import models.Post;

import java.util.List;

public interface PostsRepository extends CrudRepository<Post, Long> {
    List<Post> findAllByAuthorId(String id);
}
