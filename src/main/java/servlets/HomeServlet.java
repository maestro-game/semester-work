package servlets;

import html.HtmlManager;
import html.Page;
import utils.LoginManager;
import utils.RegisterManager;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HomeServlet extends HttpServlet {
    HtmlManager htmlManager;
    RegisterManager registerManager;
    LoginManager loginManager;

    @Override
    public void init(ServletConfig config) {
        htmlManager = (HtmlManager) config.getServletContext().getAttribute("htmlManager");
//        registerManager = new SimpleRegisterManager(dataManager);
//        loginManager = new SimpleLoginManager(dataManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        Page page = Page.home;
        String param = (String) request.getAttribute("id");

        if (!request.getRequestURI().equals("/")) {
            page = Page.notFound;
        } else {
            if (param != null) {
                page = Page.profile;
            }
        }
        htmlManager.render(page, param, request, response, root);
    }
}
