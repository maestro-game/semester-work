package html;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import models.Post;
import models.User;
import repositories.CommentsRepository;
import repositories.PostsRepository;
import repositories.UsersRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HtmlManagerIml implements HtmlManager {
    UsersRepository usersRepository;
    CommentsRepository commentsRepository;
    PostsRepository postsRepository;

    public void render(Page page, HttpServletRequest request, HttpServletResponse response, Map<String, Object> root) {
        render(page, null, request, response, root);
    }

    public void render(Page page, String param, HttpServletRequest request, HttpServletResponse response, Map<String, Object> root) {
        response.setCharacterEncoding("UTF-8");
        String name = (String) request.getAttribute("name");
        root.put("logged", name != null);

        switch (page) {
            case messages -> {
                if (name != null) {
                    Optional<User> candidate = usersRepository.findById(name);
                    if (candidate.isEmpty()) {
                        throw new IllegalArgumentException();
                    }
                    User user = candidate.get();
                    root.put("user", user);
                    //TODO dodelay, blyat
                } else {
                    List<String> warnings = new LinkedList<>();
                    warnings.add("Вы должны быть авторизованы.");
                    root.put("warning", warnings);
                    page = Page.login;
                }
            }
            case profile -> {
                Optional<User> candidate = usersRepository.findById(param);
                if (candidate.isEmpty()) {
                    page = Page.notFound;
                } else {
                    if (param.equals(name)) {
                        root.put("itself", null);
                    }
                    root.put("profile", candidate.get());
                    root.put("posts", postsRepository.findAllByAuthorId(param));
                }
            }
            case post -> {
                Optional<Post> candidate = postsRepository.findById(Long.parseLong(param));
                if (candidate.isEmpty()) {
                    page = Page.notFound;
                } else {
                    Post post = candidate.get();
                    root.put("post", post);
                    root.put("comments", commentsRepository.findAllByPostId(post.getId()));
                }
            }
        }

        try {
            ((Configuration) request.getServletContext().getAttribute("cfg")).getTemplate(page.path).process(root, response.getWriter());
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }

    public HtmlManagerIml(UsersRepository usersRepository, CommentsRepository commentsRepository, PostsRepository postsRepository) {
        this.usersRepository = usersRepository;
        this.commentsRepository = commentsRepository;
        this.postsRepository = postsRepository;
    }
}