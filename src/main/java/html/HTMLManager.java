package html;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import models.User;
import repositories.CommentsRepository;
import repositories.PostsRepository;
import repositories.UsersRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

public class HTMLManager implements HtmlGenerator {
    UsersRepository usersRepository;
    CommentsRepository commentsRepository;
    PostsRepository postsRepository;

    public void render(Page page, HttpServletRequest request, HttpServletResponse response, Map<String, Object> root) {
        render(page, null, request, response, root);
    }

    public void render(Page page, String param, HttpServletRequest request, HttpServletResponse response, Map<String, Object> root) {
        response.setCharacterEncoding("UTF-8");
        switch (page) {
            case messages -> {
                //root.put("messages", .getMessages());
            }
            case profile -> {
                Optional<User> candidate = usersRepository.findById(param);
                if (candidate.isEmpty()) {
                    page = Page.notFound;
                } else {
                    root.put("profile", candidate.get());
                    root.put("posts", postsRepository.findAllByAuthorId(param));
                }
            }
        }

        try {
            ((Configuration) request.getServletContext().getAttribute("cfg")).getTemplate(page.path).process(root, response.getWriter());
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }

    public HTMLManager(UsersRepository usersRepository, CommentsRepository commentsRepository, PostsRepository postsRepository) {
        this.usersRepository = usersRepository;
        this.commentsRepository = commentsRepository;
        this.postsRepository = postsRepository;
    }
}
