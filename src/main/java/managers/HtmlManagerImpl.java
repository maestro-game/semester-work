package managers;

import lombok.AllArgsConstructor;
import models.Post;
import models.User;
import repositories.*;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HtmlManagerImpl implements HtmlManager {
    UsersRepository usersRepository;
    CommentsRepository commentsRepository;
    PostsRepository postsRepository;
    LikesRepository likesRepository;
    ImageRepository imageRepository;

    public Page render(Page page, User user, Map<String, Object> root) {
        return render(page, user, null, root);
    }

    public Page render(Page page, User user, String param, Map<String, Object> root) {
        switch (page) {
            case login, register, home -> {
                if (user != null) {
                    return render(Page.profile, user, user.getId(), root);
                }
            }
            case profile -> {
                if (user.getId().equals(param)) {
                    if (param.equals(user.getId())) {
                        root.put("isOwner", true);
                    }
                    root.put("owner", user);
                    root.put("posts", postsRepository.findAllByAuthorId(param));
                } else {
                    Optional<User> candidate = usersRepository.findById(param);
                    if (candidate.isEmpty()) {
                        page = Page.notFound;
                    } else {
                        User user1 = candidate.get();
                        user1.setImage(imageRepository.pathForUser(user.getId(), user.getImage()));
                        root.put("owner", user1);
                        root.put("posts", postsRepository.findAllByAuthorId(param));
                    }
                }
            }
            case post -> {
                Long postId = Long.parseLong(param);
                Optional<Post> candidate = postsRepository.findById(postId);
                if (candidate.isEmpty()) {
                    page = Page.notFound;
                } else {
                    Post post = candidate.get();
                    post.setImage(imageRepository.pathForPost(postId, post.getImage()));
                    if (post.getAuthor().getId().equals(user.getId())) {
                        root.put("isOwner", true);
                    }
                    root.put("post", post);
                    root.put("comments", commentsRepository.findAllByPostId(post.getId()));
                    root.put("likes", likesRepository.countByPostId(postId));
                }
            }
        }
        root.put("user", user);
        return page;
    }
}
