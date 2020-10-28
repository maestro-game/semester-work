package servlets;

import managers.HtmlManager;
import managers.Page;
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

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        registerManager = (RegisterManager) context.getAttribute("registerManager");
        passwordEncoder = (PasswordEncoder) context.getAttribute("passwordEncoder");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        htmlManager.render(Page.register, request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        Page page = Page.register;
        List<String> warnings = new LinkedList<>();
        try {
            String id = request.getParameter("id");
            User user = User.builder().id(id)
                    .password(passwordEncoder.encode(request.getParameter("password")))
                    .name(request.getParameter("name"))
                    .surname(request.getParameter("surname"))
                    .email(request.getParameter("email"))
                    .birth(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("birth")).getTime()))
                    .build();

            if (registerManager.register(user, warnings)) {
                page = Page.login;
            } else {
                root.put("id", id);
                root.put("name", user.getName());
                root.put("surname", user.getSurname());
                root.put("email", user.getEmail());
                root.put("birth", user.getBirth().toString());
                root.put("warnings", warnings);
            }
        } catch (ParseException e) {
            warnings.add("Неверный формат даты (yyyy-MM-dd))");
            root.put("warnings", warnings);
        }
        htmlManager.render(page, request, response, root);
    }
}
