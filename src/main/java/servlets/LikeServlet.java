package servlets;

import models.Like;
import models.Post;
import models.User;
import repositories.LikesRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class LikeServlet extends HttpServlet {
    LikesRepository likesRepository;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        likesRepository = (LikesRepository) context.getAttribute("likesRepository");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getAttribute("user");
        if (user != null) {
            try {
                likesRepository.save(new Like(user, Post.builder().id(Long.valueOf(request.getParameter("postId"))).build()));
                response.setStatus(200);
            } catch (IllegalArgumentException e) {
                if (e.getCause().getClass() != SQLException.class) {
                    throw e;
                }
                response.setStatus(400);
            }
        } else {
            response.getWriter().write("redirect");
            response.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getAttribute("user");
        if (user != null) {
            try {
                likesRepository.deleteById(new Like(user, Post.builder().id(Long.valueOf(request.getParameter("postId"))).build()));
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
    }
}
