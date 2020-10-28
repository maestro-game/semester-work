package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import managers.HtmlManager;
import models.Comment;
import models.Post;
import models.User;
import repositories.CommentsRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class CommentServlet extends HttpServlet {
    HtmlManager htmlManager;
    CommentsRepository commentsRepository;
    ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        commentsRepository = (CommentsRepository) context.getAttribute("commentRepository");
        objectMapper = (ObjectMapper) context.getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        User user = (User) request.getAttribute("user");
        if (user != null) {
            String answers = request.getParameter("answers");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            try {
                commentsRepository.save(new Comment(null,
                        user,
                        timestamp,
                        Post.builder().id(Long.valueOf(request.getParameter("post"))).build(),
                        answers != null ? Comment.builder().id(Long.valueOf(answers)).build() : null,
                        request.getParameter("text")));
            } catch (IllegalArgumentException e) {
                if (e.getCause().getClass() != SQLException.class) {
                    throw e;
                }
                response.setStatus(400);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM в HH:mm:ss");
            //TODO add timestamp and user to response
            response.getWriter().write(dateFormat.format(timestamp));
            response.setStatus(200);
        } else {
            //TODO redirect
            response.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getAttribute("user");
        if (user != null) {
            Long commentId = Long.valueOf(request.getParameter("id"));
            Optional<Comment> candidate;
            try {
                candidate = commentsRepository.findById(commentId);
                if (candidate.isPresent() && candidate.get().getAuthor().getId().equals(user.getId())) {
                    try {
                        commentsRepository.deleteById(commentId);
                        response.setStatus(200);
                    } catch (IllegalArgumentException e) {
                        if (e.getCause().getClass() != SQLException.class) {
                            throw e;
                        }
                        response.setStatus(400);
                    }
                }
            } catch (IllegalArgumentException e) {
                if (e.getCause().getClass() != SQLException.class) {
                    throw e;
                } else {
                    response.setStatus(400);
                }
            }
        } else {
            //TODO redirect
            response.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getAttribute("user");
        if (user != null) {
            try {
                commentsRepository.updateText(Long.valueOf(request.getParameter("id")), request.getParameter("text"));
                response.setStatus(200);
            } catch (IllegalArgumentException e) {
                if (e.getCause().getClass() != SQLException.class) {
                    throw e;
                }
                response.setStatus(400);
            }
        } else {
            //TODO redirect
            response.setStatus(400);
        }
    }
}