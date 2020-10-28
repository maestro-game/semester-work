package repositories;

import models.Like;

import java.util.List;

public interface LikesRepository {
    boolean isLiked(String userId, Long postId);
    List<Like> findAllByUserId(String userId);
    List<Like> findALlByPostId(Long postId);
    Integer countByPostId(Long postId);
    void save(Like like);
    void delete(Like like);
}
