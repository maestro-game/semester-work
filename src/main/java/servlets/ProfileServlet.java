package servlets;

import managers.*;
import models.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileServlet extends HttpServlet {
    HtmlManager htmlManager;
    TemplateManager templateManager;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        templateManager = (TemplateManager) context.getAttribute("templateManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        int pageSize = 15;
        int page = Integer.parseInt(request.getParameter("page"));
        root.put("offset", pageSize*(page - 1));
        root.put("limit", pageSize);
        templateManager.write(htmlManager.render(Page.profile, user, request.getRequestURI().substring(4), root), request, response, root);
    }
}
