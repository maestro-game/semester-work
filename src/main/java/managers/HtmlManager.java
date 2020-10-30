package managers;

import models.User;

import java.util.Map;

public interface HtmlManager {
    Page render(Page page, User user, Map<String, Object> root);

    Page render(Page page, User user, String param, Map<String, Object> root);

}
