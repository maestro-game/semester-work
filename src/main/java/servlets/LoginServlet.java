package servlets;

import managers.HtmlManager;
import managers.Page;
import models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import managers.CookieManager;
import managers.LoginManager;
import repositories.UsersRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    HtmlManager htmlManager;
    LoginManager loginManager;
    CookieManager cookieManager;
    PasswordEncoder passwordEncoder;
    UsersRepository usersRepository;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        loginManager = (LoginManager) context.getAttribute("loginManager");
        cookieManager = (CookieManager) context.getAttribute("cookieManager");
        passwordEncoder = (PasswordEncoder) context.getAttribute("passwordEncoder");
        usersRepository = (UsersRepository) context.getAttribute("usersRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        Page page = Page.login;
        User user = (User) request.getAttribute("user");
        if (user != null) {
            page = Page.profile;
        }
        htmlManager.render(page, user != null ? user.getId() : null, request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        if (user != null) {
            htmlManager.render(Page.profile, user.getId(), request, response, root);
            return;
        }
        List<String> warnings = new LinkedList<>();
        Page page;
        String id = request.getParameter("id");
        if (loginManager.isExist(id, request.getParameter("password"), warnings)) {
            HttpSession session = request.getSession();
            User user1 = usersRepository.findById(id).get();
            session.setAttribute("user", user1);
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
