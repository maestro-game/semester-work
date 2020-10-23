package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import html.HtmlManager;
import models.Comment;
import models.Post;
import models.User;
import repositories.CommentsRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CommentServlet extends HttpServlet {
    HtmlManager htmlManager;
    CommentsRepository commentsRepository;
    ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) {
        htmlManager = (HtmlManager) config.getServletContext().getAttribute("htmlManager");
        commentsRepository = (CommentsRepository) config.getServletContext().getAttribute("commentRepository");
        objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        String id = (String) request.getAttribute("id");
        if (id != null) {
            String answers = request.getParameter("answers");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            commentsRepository.save(new Comment(null,
                    User.builder().id(id).build(),
                    timestamp,
                    Post.builder().id(Long.valueOf(request.getParameter("post"))).build(),
                    answers != null ? Comment.builder().id(Long.valueOf(request.getParameter("answers"))).build() : null,
                    request.getParameter("text")));
            response.setStatus(200);
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM в HH:mm:ss");
                response.getWriter().write(dateFormat.format(timestamp));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            response.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = (String) request.getAttribute("id");
        if (id != null) {
            try {
                commentsRepository.deleteById(Long.valueOf(request.getParameter("id")));
                //TODO return true to user
            } catch (IllegalArgumentException e) {
                if (e.getCause().getClass() == SQLException.class) {
                    //TODO return false
                }
            }
        }
    }
}
