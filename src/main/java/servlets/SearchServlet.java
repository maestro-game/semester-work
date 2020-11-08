package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import managers.HtmlManager;
import managers.Page;
import managers.TemplateManager;
import models.User;
import repositories.CategoryRepository;
import repositories.PostsRepository;
import repositories.Taxon;
import repositories.UsersRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SearchServlet extends HttpServlet {
    static final int PAGE_SIZE = 15;

    HtmlManager htmlManager;
    CategoryRepository categoryRepository;
    PostsRepository postsRepository;
    TemplateManager templateManager;
    UsersRepository usersRepository;
    ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        postsRepository = (PostsRepository) context.getAttribute("postsRepository");
        templateManager = (TemplateManager) context.getAttribute("templateManager");
        usersRepository = (UsersRepository) context.getAttribute("usersRepository");
        objectMapper = (ObjectMapper) context.getAttribute("objectMapper");
        categoryRepository = (CategoryRepository) context.getAttribute("categoryRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        templateManager.write(htmlManager.render(Page.search, user, request.getRequestURI().substring(6), root), request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String result;
        switch (request.getParameter("type")) {
            case "user": {
                String temp = request.getParameter("page");
                int page = temp == null ? 1 : Integer.parseInt(temp);
                result = objectMapper.writeValueAsString(usersRepository.searchById(request.getParameter("id"), PAGE_SIZE * (page - 1), PAGE_SIZE));
                break;
            }
            case "category": {
                String temp = request.getParameter("page");
                int page = temp == null ? 1 : Integer.parseInt(temp);
                Taxon taxon = Taxon.values()[Integer.parseInt(request.getParameter("taxon"))];
                long id = Long.parseLong(request.getParameter("id"));
                result = objectMapper.writeValueAsString(postsRepository.findPageByCategory(taxon, id, PAGE_SIZE * (page - 1), PAGE_SIZE));
                break;
            }
            case "getCats": {
                Taxon taxon = Taxon.values()[Integer.parseInt(request.getParameter("taxon"))];
                long id = Long.parseLong(request.getParameter("id"));
                result = objectMapper.writeValueAsString(categoryRepository.findChildCategories(taxon, id));
                break;
            }
            default:
                response.setStatus(400);
                result = "unexpected type";
                break;
        }
        response.getWriter().write(result);
    }
}
