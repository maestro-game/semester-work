package servlets;

import managers.HtmlManager;
import managers.Page;
import managers.TemplateManager;
import models.Category;
import models.FollowCat;
import models.FollowUser;
import models.User;
import repositories.FollowCatsRepository;
import repositories.FollowUsersRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FollowsServlet extends HttpServlet {
    FollowUsersRepository followUsersRepository;
    FollowCatsRepository followCatsRepository;
    TemplateManager templateManager;
    HtmlManager htmlManager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        followCatsRepository = (FollowCatsRepository) context.getAttribute("followCatsRepository");
        followUsersRepository = (FollowUsersRepository) context.getAttribute("followUsersRepository");
        templateManager = (TemplateManager) context.getAttribute("templateManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login");
        } else {
            templateManager.write(htmlManager.render(Page.follow, user, request.getRequestURI().substring(6), root), request, response, root);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getAttribute("user");
        if (user != null) {
            switch (request.getParameter("type")) {
                case "user":
                    try {
                        followUsersRepository.save(new FollowUser(user, User.builder().id(request.getParameter("follow")).build()));
                    } catch (IllegalArgumentException e) {
                        if (e.getCause().getClass() != SQLException.class || ((SQLException) e.getCause()).getErrorCode() != 23505) {
                            throw e;
                        }
                        response.getWriter().write("вы уже подписаны на этого пользователя");
                        response.setStatus(400);
                    }
                    break;
                case "category":
                    try {
                        followCatsRepository.save(new FollowCat(user, new Category(Long.parseLong(request.getParameter("follow")), null)));
                    } catch (IllegalArgumentException e) {
                        if (e.getCause().getClass() != SQLException.class || ((SQLException) e.getCause()).getErrorCode() != 23505) {
                            throw e;
                        }
                        response.getWriter().write("вы уже подписаны на эту категорию");
                        response.setStatus(400);
                    }
                    break;
            }
        } else {
            response.getWriter().write("redirect");
            response.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
