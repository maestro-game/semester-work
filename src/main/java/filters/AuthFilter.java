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
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);
        if (session != null) {
            String name = (String) session.getAttribute("name");
            if (name != null) {
                request.setAttribute("name", name);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
