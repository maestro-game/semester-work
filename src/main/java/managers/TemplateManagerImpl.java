package managers;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
public class TemplateManagerImpl implements TemplateManager{

    @Override
    public void write(Page page, HttpServletRequest request, HttpServletResponse response, Map<String, Object> root) {
        try {
            ((Configuration) request.getServletContext().getAttribute("cfg")).getTemplate(page.path).process(root, response.getWriter());
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }
}
