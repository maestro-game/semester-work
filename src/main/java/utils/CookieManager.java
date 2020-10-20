package utils;

import javax.servlet.http.Cookie;
import java.util.List;

public interface CookieManager {
    Cookie assign(Object userId);

    void remove(Cookie cookie);

    List<Cookie> getAllByUserId(Object userId);
}
