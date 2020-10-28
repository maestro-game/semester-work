package managers;

import javax.servlet.http.Cookie;
import java.util.List;

public interface CookieManager {
    Cookie assign(Object userId);

    void remove(Cookie cookie);

    List<String> getAllByUserId(Object userId);
}
