package filters;

import utils.CookieManager;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

public class AuthFilter implements Filter {
    CookieManager cookieManager;

    @Override
    public void init(FilterConfig filterConfig) {
        cookieManager = (CookieManager) filterConfig.getServletContext().getAttribute("cookieManager");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);
        if (session != null) {
            String id = (String) session.getAttribute("id");
            if (id != null) {
                request.setAttribute("id", id);
            }
        } else {
            HashMap<String, String> hashMap = new HashMap();
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    hashMap.put(cookie.getName(), cookie.getValue());
                }
                String id = hashMap.get("id");
                if (id != null) {
                    for (String cookie : cookieManager.getAllByUserId(id)) {
                        if (hashMap.get(cookie) != null) {
                            request.setAttribute("id", id);
                            request.getSession().setAttribute("id", id);
                            break;
                        }
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
