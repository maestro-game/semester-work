package servlets;

import managers.HtmlManager;
import managers.Page;
import managers.TemplateManager;
import models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import managers.RegisterManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            User candidate = User.builder().id(request.getParameter("id"))
                    .password(passwordEncoder.encode(request.getParameter("password")))
                    .name(request.getParameter("name"))
                    .surname(request.getParameter("surname"))
                    .email(request.getParameter("email"))
                    .build();
            try {
                candidate.setBirth(
                        new Date(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("birth")).getTime()));
                if (registerManager.register(candidate, warnings)) {
                    page = Page.login;
                } else {
                    root.put("warnings", warnings);
                    root.put("id", candidate.getId());
                    root.put("name", candidate.getName());
                    root.put("surname", candidate.getSurname());
                    root.put("email", candidate.getEmail());
                    root.put("birth", candidate.getBirth());
                }
            } catch (ParseException e) {
                warnings.add("Неверный формат даты (yyyy-MM-dd))");
                root.put("warnings", warnings);
                root.put("id", candidate.getId());
                root.put("name", candidate.getName());
                root.put("surname", candidate.getSurname());
                root.put("email", candidate.getEmail());
                root.put("birth", "");
            }
        }

        templateManager.write(htmlManager.render(page, user, root), request, response, root);
    }
}
