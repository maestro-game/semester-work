package servlets;

import managers.HtmlManager;
import managers.Page;
import managers.TemplateManager;
import models.User;
import repositories.ImageRepository;
import repositories.UsersRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProfileInfoServlet extends HttpServlet {
    HtmlManager htmlManager;
    TemplateManager templateManager;
    ImageRepository imageRepository;
    UsersRepository usersRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        templateManager = (TemplateManager) context.getAttribute("templateManager");
        imageRepository = (ImageRepository) context.getAttribute("imageRepository");
        usersRepository = (UsersRepository) context.getAttribute("usersRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        templateManager.write(htmlManager.render(Page.profileInfo, user, request.getRequestURI().substring(4), root), request, response, root);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User id = (User) request.getAttribute("user");
        if (id != null) {
            User user = (User) request.getAttribute("user");
            String field = request.getParameter("field");
            try {
                int status;
                switch (field) {
                    case "name", "surname", "middleName", "about", "birth" -> {
                        usersRepository.updateField(user.getId(), field, request.getParameter("data"));
                        status = 200;
                    }
                    case "image" -> {
                        Part part = request.getPart("image");
                        if (part.getSize() == 0) {
                            //TODO send error
                            response.setStatus(400);
                            return;
                        }
                        String sfn = part.getSubmittedFileName();
                        usersRepository.updateField(user.getId(), field, sfn.substring(sfn.lastIndexOf('.')));
                        imageRepository.saveForUser(part, user.getId());
                        status = 200;
                    }
                    default -> {
                        //TODO send error
                        status = 400;
                    }
                }
                response.setStatus(status);
            } catch (IllegalArgumentException e) {
                if (e.getCause().getClass() != SQLException.class) {
                    throw e;
                }
                response.setStatus(400);
            }
        } else {
            response.getWriter().write("redirect");
            response.setStatus(400);
        }
    }
}
