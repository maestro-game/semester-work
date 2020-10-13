package servlets;

import html.HtmlGenerator;
import html.Page;
import utils.LoginManager;
import utils.RegisterManager;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    HtmlGenerator htmlManager;
    LoginManager loginManager;

    @Override
    public void init(ServletConfig config) {
        htmlManager = (HtmlGenerator) config.getServletContext().getAttribute("htmlManager");
        loginManager = (LoginManager) config.getServletContext().getAttribute("loginManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        Map<String, Object> root = new HashMap<>();
        htmlManager.render(Page.login, request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        Map<String, Object> root = new HashMap<>();
        List<String> warnings = new LinkedList<>();
        Page page;
        String id = request.getParameter("id");
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException();
        }
        byte[] hash = digest.digest(request.getParameter("password").getBytes(StandardCharsets.UTF_8));
        if (loginManager.isExist(id, hash, warnings)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", id);
            page = Page.profile;
        } else {
            warnings.add("Не верный логин или пароль.");
            root.put("warnings", warnings);
            root.put("id", id);
            page = Page.login;
        }
        htmlManager.render(page, request, response, root);
    }
}
