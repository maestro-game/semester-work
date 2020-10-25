package servlets;

import html.HtmlManager;
import html.Page;
import models.Post;
import repositories.PostsRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PostServlet extends HttpServlet {
    HtmlManager htmlManager;
    PostsRepository postsRepository;

    @Override
    public void init(ServletConfig config) {
        htmlManager = (HtmlManager) config.getServletContext().getAttribute("htmlManager");
        postsRepository = (PostsRepository) config.getServletContext().getAttribute("postsRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        htmlManager.render(Page.post, request.getRequestURI().substring(6), request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = (String) request.getAttribute("id");
        if (id != null ) {
            Optional<Post> post = postsRepository.findById(Long.valueOf(request.getParameter("id")));
            if (post.isPresent() && post.get().getAuthor().getId().equals(id)) {
                postsRepository.deleteById(post.get().getId());
                response.setStatus(200);
            } else {
                response.setStatus(400);
            }
        } else {
            response.sendRedirect("/login");
        }
    }
}
