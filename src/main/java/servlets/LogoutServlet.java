package servlets;

import html.HtmlManager;
import html.Page;
import utils.CookieManager;

import javax.servlet.ServletConfig;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LogoutServlet extends HttpServlet {
    HtmlManager htmlManager;
    CookieManager cookieManager;

    @Override
    public void init(ServletConfig config) {
        htmlManager = (HtmlManager) config.getServletContext().getAttribute("htmlManager");
        cookieManager = (CookieManager) config.getServletContext().getAttribute("cookieManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        String id = (String) request.getAttribute("id");
        if (id != null) {

            HashSet<String> hashSet = new HashSet<>(cookieManager.getAllByUserId(id));
            for (Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                if (hashSet.contains(cookie.getName())) {
                    cookie.setValue(id);
                    cookieManager.remove(cookie);
                }
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        htmlManager.render(Page.home, request, response, root);
    }
}
