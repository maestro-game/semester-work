package filters;

import managers.CookieManager;
import models.User;
import repositories.UsersRepository;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;

public class AuthFilter implements Filter {
    CookieManager cookieManager;
    UsersRepository usersRepository;

    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext context = filterConfig.getServletContext();
        cookieManager = (CookieManager) context.getAttribute("cookieManager");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // TODO REMOVE \/\/\/\/\/
        request.setAttribute("user", new User("bronzehair", null, "Vadim", "Belov", null, "jpg", "test@mail.ru", Date.valueOf("2001-01-30"), "test user", null, null, null));
        if (true) {
            filterChain.doFilter(request, response);
            return;
        }
        // TODO REMOVE /\/\/\/\/\

        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                request.setAttribute("user", user);
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
                            User user = usersRepository.findById(id).get();
                            request.setAttribute("user", user);
                            request.getSession().setAttribute("user", user);
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
