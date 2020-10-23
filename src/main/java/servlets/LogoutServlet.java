package servlets;

import html.HtmlManager;
import html.Page;
import utils.CookieManager;

import javax.servlet.ServletConfig;
import javax.servlet.http.*;
import java.util.HashMap;
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
        String id = request.getParameter("id");
        if (id != null) {
            HashMap<String, String> hashMap = new HashMap<>();

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            cookieManager.getAllByUserId(id).forEach(cookie -> hashMap.put(cookie.getName(), cookie.getValue()));
            for (Cookie cookie : request.getCookies()) {
                if (hashMap.get(cookie.getName()) != null) {
                    cookieManager.remove(cookie);
                }
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        htmlManager.render(Page.home, request, response, root);
    }
}
