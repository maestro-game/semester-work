package repositories;

import models.Comment;

import java.util.List;

public interface CommentsRepository extends CrudRepository<Comment, Long> {
    List<Comment> findPageByAuthorId(String authorId, int limit, int offset);

    List<Comment> findPageByPostId(Long id, int limit, int offset);

    Long saveReturningId(Comment comment);

    void updateText(Long id, String text);
}
