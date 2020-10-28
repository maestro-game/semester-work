package servlets;

import managers.HtmlManager;
import managers.Page;
import managers.LoginManager;
import managers.RegisterManager;
import models.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HomeServlet extends HttpServlet {
    HtmlManager htmlManager;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        Page page = Page.home;
        User user = (User) request.getAttribute("user");

        if (!request.getRequestURI().equals("/")) {
            page = Page.notFound;
        } else {
            if (user != null) {
                page = Page.profile;
            }
        }
        htmlManager.render(page, user != null ? user.getId() : null, request, response, root);
    }
}
