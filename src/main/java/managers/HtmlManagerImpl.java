package managers;

import lombok.AllArgsConstructor;
import models.Post;
import models.User;
import repositories.CommentsRepository;
import repositories.LikesRepository;
import repositories.PostsRepository;
import repositories.UsersRepository;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HtmlManagerImpl implements HtmlManager {
    UsersRepository usersRepository;
    CommentsRepository commentsRepository;
    PostsRepository postsRepository;
    LikesRepository likesRepository;

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
                        root.put("owner", candidate.get());
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
