package managers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface TemplateManager {
    void write(Page page, HttpServletRequest request, HttpServletResponse response, Map<String, Object> root);
}
