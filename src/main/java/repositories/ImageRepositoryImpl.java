package repositories;

import lombok.AllArgsConstructor;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
public class ImageRepositoryImpl implements ImageRepository{
    String basicPath;

    private void hateDuplicates(Part p, String id, String theDifference) {
        if (p.getSize() != 0) {
            String dirName = basicPath + theDifference + id;
            File file = new File(dirName);
            file.mkdirs();
            String sfn = p.getSubmittedFileName();
            String extension = sfn.substring(sfn.lastIndexOf('.'));
            try {
                p.write(dirName + "/" + id + "." + extension);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private String againHateDuplicates(String id, String extension, String theDifference) {
        return basicPath + theDifference + id + "/" + id + "." + extension;
    }

    @Override
    public void saveForUser(Part p, String userId) {
        hateDuplicates(p, userId, "/users/");
    }

    @Override
    public void saveForPost(Part p, Long postId) {
        hateDuplicates(p, postId.toString(), "/posts/");
    }

    @Override
    public String pathForPost(Long postId, String extension) {
        return againHateDuplicates(postId.toString(), extension, "/posts/");
    }

    @Override
    public String pathForUser(String userId, String extension) {
        return againHateDuplicates(userId, extension, "/users/");
    }
}
