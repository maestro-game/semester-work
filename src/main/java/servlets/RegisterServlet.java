package servlets;

import managers.HtmlManager;
import managers.Page;
import managers.RegisterManager;
import managers.TemplateManager;
import models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RegisterServlet extends HttpServlet {
    HtmlManager htmlManager;
    RegisterManager registerManager;
    PasswordEncoder passwordEncoder;
    TemplateManager templateManager;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        registerManager = (RegisterManager) context.getAttribute("registerManager");
        passwordEncoder = (PasswordEncoder) context.getAttribute("passwordEncoder");
        templateManager = (TemplateManager) context.getAttribute("templateManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        templateManager.write(htmlManager.render(Page.register, user, root), request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        Page page = Page.register;

        List<String> warnings = new LinkedList<>();

        if (user == null) {
            String email = request.getParameter("email");
            User candidate = User.builder().id(request.getParameter("id"))
                    .password(passwordEncoder.encode(request.getParameter("password")))
                    .name(request.getParameter("name"))
                    .surname(request.getParameter("surname"))
                    .email(email)
                    .build();
            if (email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])") //how did u get here? XD
                    && registerManager.register(candidate, warnings)) {
                page = Page.login;
            } else {
                root.put("warnings", warnings);
                root.put("id", candidate.getId());
                root.put("name", candidate.getName());
                root.put("surname", candidate.getSurname());
                root.put("email", candidate.getEmail());
            }
        }

        templateManager.write(htmlManager.render(page, user, root), request, response, root);
    }
}
