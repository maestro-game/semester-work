package servlets;

import managers.HtmlManager;
import managers.Page;
import managers.CookieManager;
import managers.TemplateManager;
import models.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LogoutServlet extends HttpServlet {
    HtmlManager htmlManager;
    CookieManager cookieManager;
    TemplateManager templateManager;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        cookieManager = (CookieManager) context.getAttribute("cookieManager");
        templateManager = (TemplateManager) context.getAttribute("templateManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        if (user != null) {

            HashSet<String> hashSet = new HashSet<>(cookieManager.getAllByUserId(user.getId()));
            for (Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                if (hashSet.contains(cookie.getName())) {
                    cookie.setValue(user.getId());
                    cookieManager.remove(cookie);
                }
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        templateManager.write(htmlManager.render(Page.home, null, root), request, response, root);
    }
}
