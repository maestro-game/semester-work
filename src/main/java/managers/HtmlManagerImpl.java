package managers;

import lombok.AllArgsConstructor;
import models.Post;
import models.User;
import repositories.*;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HtmlManagerImpl implements HtmlManager {
    final static int PAGE_SIZE = 15;

    UsersRepository usersRepository;
    CommentsRepository commentsRepository;
    PostsRepository postsRepository;
    LikesRepository likesRepository;
    ImageRepository imageRepository;
    CategoryRepository categoryRepository;
    FollowUsersRepository followUsersRepository;
    FollowCatsRepository followCatsRepository;

    public Page render(Page page, User user, Map<String, Object> root) {
        return render(page, user, null, root);
    }

    public Page render(Page page, User user, String param, Map<String, Object> root) {
        switch (page) {
            case login:
            case register:
            case home:
                if (user != null) {
                    return render(Page.profile, user, user.getId(), root);
                }
                break;
            case follow:
                root.put("followsCategories", followCatsRepository.findByUserId(user.getId()));
                root.put("followsUsers", followUsersRepository.findByUserId(user.getId()));
                break;
            case profile:
            case profileInfo:
                if (user == null) {
                    page = Page.login;
                    break;
                }
                if (param.equals(user.getId())) {
                    root.put("isOwner", true);
                    root.put("owner", user);
                    Integer pageNum = (Integer) root.get("page");
                    root.put("posts", postsRepository.findPageByAuthorId(param, PAGE_SIZE * ((pageNum == null ? 1 : pageNum) - 1), PAGE_SIZE));
                } else {
                    Optional<User> candidate = usersRepository.findById(param);
                    if (candidate.isEmpty()) {
                        page = Page.notFound;
                    } else {
                        User user1 = candidate.get();
                        user1.setImage(imageRepository.pathForUser(user.getId(), user.getImage()));
                        root.put("owner", user1);
                        Integer pageNum = (Integer) root.get("page");
                        root.put("posts", postsRepository.findPageByAuthorId(param, PAGE_SIZE * ((pageNum == null ? 1 : pageNum) - 1), PAGE_SIZE));
                    }
                }
                break;
            case post:
                Long postId = Long.parseLong(param);
                Optional<Post> candidate = postsRepository.findById(postId);
                if (candidate.isEmpty()) {
                    page = Page.notFound;
                } else {
                    Post post = candidate.get();
                    post.setImage(imageRepository.pathForPost(postId, post.getImage()));
                    if (user != null && post.getAuthor().getId().equals(user.getId())) {
                        root.put("isOwner", true);
                    }
                    root.put("post", post);
                    Integer pageNum = (Integer) root.get("page");
                    root.put("comments", commentsRepository.findPageByPostId(post.getId(), PAGE_SIZE * ((pageNum == null ? 1 : pageNum) - 1), PAGE_SIZE));
                    root.put("likes", likesRepository.countByPostId(postId));
                    root.put("isLiked", user != null && likesRepository.isLiked(user.getId(), postId));
                }
                break;
        }
        root.put("user", user);
        return page;
    }
}
