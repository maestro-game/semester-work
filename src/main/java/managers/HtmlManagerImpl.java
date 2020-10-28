package managers;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import models.Post;
import models.User;
import repositories.CommentsRepository;
import repositories.LikesRepository;
import repositories.PostsRepository;
import repositories.UsersRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HtmlManagerImpl implements HtmlManager {
    UsersRepository usersRepository;
    CommentsRepository commentsRepository;
    PostsRepository postsRepository;
    LikesRepository likesRepository;

    public void render(Page page, HttpServletRequest request, HttpServletResponse response, Map<String, Object> root) {
        render(page, null, request, response, root);
    }

    public void render(Page page, String param, HttpServletRequest request, HttpServletResponse response, Map<String, Object> root) {
        response.setCharacterEncoding("UTF-8");
        User user = (User) request.getAttribute("user");
        root.put("user", user);

        switch (page) {
            case messages -> {
                //TODO
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

        try {
            ((Configuration) request.getServletContext().getAttribute("cfg")).getTemplate(page.path).process(root, response.getWriter());
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }
}
