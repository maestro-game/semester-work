package html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface HtmlGenerator {
    void render(Page page, HttpServletRequest request, HttpServletResponse response, Map<String, Object> root);

    void render(Page page, String param, HttpServletRequest request, HttpServletResponse response, Map<String, Object> root);

}
