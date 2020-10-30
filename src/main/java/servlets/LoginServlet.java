package servlets;

import managers.*;
import models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    TemplateManager templateManager;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        loginManager = (LoginManager) context.getAttribute("loginManager");
        cookieManager = (CookieManager) context.getAttribute("cookieManager");
        passwordEncoder = (PasswordEncoder) context.getAttribute("passwordEncoder");
        usersRepository = (UsersRepository) context.getAttribute("usersRepository");
        templateManager = (TemplateManager) context.getAttribute("templateManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        templateManager.write(htmlManager.render(Page.login, user, user != null ? user.getId() : null, root), request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        String param = null;
        if (user == null) {
            List<String> warnings = new LinkedList<>();
            String id = request.getParameter("id");
            user = loginManager.login(id, request.getParameter("password"), warnings);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                param = id;
                if ("true".equals(request.getParameter("remember"))) {
                    Cookie cookie = cookieManager.assign(id);
                    cookie.setMaxAge(-1);
                    response.addCookie(cookie);
                    cookie = new Cookie("id", id);
                    cookie.setMaxAge(-1);
                    response.addCookie(cookie);
                }
            } else {
                root.put("warnings", warnings);
                root.put("id", id);
            }
        }

        templateManager.write(htmlManager.render(Page.login, user, param, root), request, response, root);
    }
}
