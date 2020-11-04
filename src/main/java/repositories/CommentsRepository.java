package repositories;

import models.Comment;

import java.util.List;

public interface CommentsRepository extends CrudRepository<Comment, Long> {
    List<Comment> findAllByAuthorId(String author);

    List<Comment> findAllByPostId(Long id);

    Long saveReturningId(Comment comment);

    void updateText(Long id, String text);
}
