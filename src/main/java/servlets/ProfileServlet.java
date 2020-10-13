package servlets;

import html.HtmlGenerator;
import html.Page;
import utils.LoginManager;
import utils.RegisterManager;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfileServlet extends HttpServlet {
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
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        Map<String, Object> root = new HashMap<>();
        htmlManager.render(Page.profile, request.getRequestURI().substring(4), request, response, root);
    }
}
