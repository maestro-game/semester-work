package servlets;

import html.HtmlManager;
import models.Comment;
import models.Post;
import models.User;
import repositories.CommentsRepository;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

public class CommentServlet extends HttpServlet {
    HtmlManager htmlManager;
    CommentsRepository commentsRepository;

    @Override
    public void init(ServletConfig config) {
        htmlManager = (HtmlManager) config.getServletContext().getAttribute("htmlManager");
        commentsRepository = (CommentsRepository) config.getServletContext().getAttribute("CommentRepository");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String id = (String) request.getAttribute("id");
        if (id != null) {
            commentsRepository.save(new Comment(null,
                    User.builder().id(id).build(),
                    new Timestamp(System.currentTimeMillis()),
                    Post.builder().id(Long.valueOf(request.getParameter("post"))).build(),
                    Comment.builder().id(Long.valueOf(request.getParameter("answers"))).build(),
                    request.getParameter("text")));
        }
    }
}
