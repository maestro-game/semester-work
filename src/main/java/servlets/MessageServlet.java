package servlets;

import managers.HtmlManager;
import managers.Page;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class MessageServlet extends HttpServlet {
    HtmlManager htmlManager;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        htmlManager.render(Page.messages, request, response, root);
    }
}
