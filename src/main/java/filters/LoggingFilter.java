package filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoggingFilter implements Filter {
    private static Logger logger;

    @Override
    public void init(FilterConfig filterConfig) {
        logger = LoggerFactory.getLogger(LoggingFilter.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String id = (String) servletRequest.getAttribute("id");
        StringBuilder builder = new StringBuilder();
        builder.append(id != null ? "authorized user '" + id + "'" : "unauthorized user" );
        builder.append(" | request on ").append(request.getRequestURI());
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            builder.append(" with cookies: [");
            boolean flag = true;
            for (Cookie cookie : cookies) {
                if (flag) {
                    flag = false;
                } else {
                    builder.append(", ");
                }
                builder.append(cookie.getName()).append(": ").append(cookie.getValue());
            }
            builder.append("]");
        } else {
            builder.append(" without cookies");
        }
        logger.debug(builder.toString());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
