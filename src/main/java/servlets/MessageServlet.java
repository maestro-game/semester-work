package servlets;//package servlets;
//
//import managers.HtmlManager;
//import managers.Page;
//import managers.TemplateManager;
//import models.User;
//
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.HashMap;
//import java.util.Map;
//
//public class MessageServlet extends HttpServlet {
//    HtmlManager htmlManager;
//    TemplateManager templateManager;
//
//    @Override
//    public void init(ServletConfig config) {
//        ServletContext context = config.getServletContext();
//        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
//        templateManager = (TemplateManager) context.getAttribute("templateManager");
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
//        Map<String, Object> root = new HashMap<>();
//        User user = (User) request.getAttribute("user");
//        templateManager.write(htmlManager.render(Page.messages, user, root), request, response, root);
//    }
//}
