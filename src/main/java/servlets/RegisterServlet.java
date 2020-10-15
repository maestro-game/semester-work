package servlets;

import html.HtmlManager;
import html.Page;
import models.User;
import utils.RegisterManager;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    @Override
    public void init(ServletConfig config) {
        htmlManager = (HtmlManager) config.getServletContext().getAttribute("htmlManager");
        registerManager = (RegisterManager) config.getServletContext().getAttribute("registerManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        Map<String, Object> root = new HashMap<>();
        htmlManager.render(Page.register, request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        Map<String, Object> root = new HashMap<>();
        Page page = Page.register;
        List<String> warnings = new LinkedList<>();
        try {
            String id = request.getParameter("id");
            //TODO i should use better hash algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(request.getParameter("password").getBytes(StandardCharsets.UTF_8));
            User user = User.builder().id(id)
                    .password(hash)
                    .name(request.getParameter("name"))
                    .surname(request.getParameter("surname"))
                    .email(request.getParameter("email"))
                    .birth(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("birth")).getTime()))
                    .build();

            if (registerManager.register(user, warnings)) {
                page = Page.login;
                root.put("id", id);
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
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        htmlManager.render(page, request, response, root);
    }
}
