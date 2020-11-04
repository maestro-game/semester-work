package repositories;

import models.Post;

import java.util.List;

public interface PostsRepository extends CrudRepository<Post, Long> {
    List<Post> findAllByAuthorId(String id);

    List<Post> findAllByCategory(Taxon taxon, Long id);

    void updateDescription(Long id, String text);

    long saveReturningId(Post post);
}
