package repositories;

import javax.servlet.http.Part;

public interface ImageRepository {
    void saveForUser(Part p, String userId);

    void saveForPost(Part p, Long postId);

    String pathForPost(Long postId, String extension);

    String pathForUser(String userId, String extension);
}
