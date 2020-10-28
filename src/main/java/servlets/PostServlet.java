package servlets;

import managers.HtmlManager;
import managers.Page;
import models.Post;
import models.User;
import repositories.PostsRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PostServlet extends HttpServlet {
    HtmlManager htmlManager;
    PostsRepository postsRepository;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        postsRepository = (PostsRepository) context.getAttribute("postsRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        htmlManager.render(Page.post, request.getRequestURI().substring(6), request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        User id = (User) request.getAttribute("user");
        if (id != null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            //TODO image
            try {
                postsRepository.save(new Post(null,
                        id,
                        timestamp,
                        null,
                        request.getParameter("description")));
            } catch (IllegalArgumentException e) {
                if (e.getCause().getClass() != SQLException.class) {
                    throw e;
                }
                response.setStatus(400);
            }
            response.setStatus(200);
        } else {
            //TODO redirect
            response.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        User id = (User) request.getAttribute("user");
        if (id != null) {
            Optional<Post> post = postsRepository.findById(Long.valueOf(request.getParameter("id")));
            if (post.isPresent() && post.get().getAuthor().getId().equals(id.getId())) {
                try {
                    postsRepository.updateDescription(post.get().getId(), request.getParameter("text"));
                } catch (IllegalArgumentException e) {
                    if (e.getCause().getClass() != SQLException.class) {
                        throw e;
                    }
                    response.setStatus(400);
                }
                response.setStatus(200);
            } else {
                response.setStatus(400);
            }
        } else {
            //TODO redirect
            response.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        User id = (User) request.getAttribute("user");
        if (id != null) {
            Optional<Post> post = postsRepository.findById(Long.valueOf(request.getParameter("id")));
            if (post.isPresent() && post.get().getAuthor().getId().equals(id.getId())) {
                try {
                    postsRepository.deleteById(post.get().getId());
                    response.setStatus(200);
                } catch (IllegalArgumentException e) {
                    if (e.getCause().getClass() != SQLException.class) {
                        throw e;
                    }
                    response.setStatus(400);
                }
            } else {
                response.setStatus(400);
            }
        } else {
            //TODO redirect
            response.setStatus(400);
        }
    }
}
