package servlets;

import html.HtmlManager;
import html.Page;
import utils.CookieManager;
import utils.LoginManager;

import javax.servlet.ServletConfig;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoginServlet extends HttpServlet {
    HtmlManager htmlManager;
    LoginManager loginManager;
    CookieManager cookieManager;

    @Override
    public void init(ServletConfig config) {
        htmlManager = (HtmlManager) config.getServletContext().getAttribute("htmlManager");
        loginManager = (LoginManager) config.getServletContext().getAttribute("loginManager");
        cookieManager = (CookieManager) config.getServletContext().getAttribute("cookieManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        Page page = Page.login;
        String param = (String) request.getAttribute("id");
        if (param != null) {
            page = Page.profile;
        }
        htmlManager.render(page, param, request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        String param = (String) request.getAttribute("id");
        if (param != null) {
            htmlManager.render(Page.profile, param, request, response, root);
            return;
        }
        List<String> warnings = new LinkedList<>();
        Page page;
        String id = request.getParameter("id");
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException();
        }
        byte[] hash = digest.digest(request.getParameter("password").getBytes(StandardCharsets.UTF_8));
        if (loginManager.isExist(id, hash, warnings)) {
            HttpSession session = request.getSession();
            session.setAttribute("id", id);
            if ("true".equals(request.getParameter("remember"))){
                Cookie cookie = cookieManager.assign(id);
                cookie.setMaxAge(-1);
                response.addCookie(cookie);
                cookie = new Cookie("id", id);
                cookie.setMaxAge(-1);
                response.addCookie(cookie);
            }
            page = Page.profile;
        } else {
            warnings.add("Не верный логин или пароль.");
            root.put("warnings", warnings);
            root.put("id", id);
            page = Page.login;
        }
        htmlManager.render(page, id, request, response, root);
    }
}
