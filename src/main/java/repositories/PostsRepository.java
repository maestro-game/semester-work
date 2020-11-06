package repositories;

import models.Post;

import java.util.List;

public interface PostsRepository extends CrudRepository<Post, Long> {
    List<Post> findPageByAuthorId(String authorId, int offset, int limit);

    List<Post> findPageByCategory(Taxon taxon, Long id, int offset, int limit);

    void updateDescription(Long id, String text);

    long saveReturningId(Post post);
}
