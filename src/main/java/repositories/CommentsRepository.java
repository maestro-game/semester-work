package repositories;

import models.Comment;

import java.util.List;

public interface CommentsRepository extends CrudRepository<Comment, Long> {
    List<Comment> findAllByAuthorId(String author);

    List<Comment> findAllByPostId(Long id);

    void updateText(Long id, String text);
}
