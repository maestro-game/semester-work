package servlets;

import html.HtmlGenerator;
import html.Page;
import utils.LoginManager;
import utils.RegisterManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HomeServlet extends HttpServlet {
    HtmlGenerator htmlManager;
    RegisterManager registerManager;
    LoginManager loginManager;

    @Override
    public void init(ServletConfig config) {
        htmlManager = (HtmlGenerator) config.getServletContext().getAttribute("htmlManager");
//        registerManager = new SimpleRegisterManager(dataManager);
//        loginManager = new SimpleLoginManager(dataManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        Page page = Page.home;
        if (!request.getRequestURI().equals("/")){
            page = Page.notFound;
        }
        htmlManager.render(page, request, response, root);
    }
}
