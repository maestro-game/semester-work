package utils;

import javax.servlet.http.Cookie;
import java.util.List;

public interface CookieManager {
    Cookie assign(Object userId);

    List<Cookie> getAllByUserId(Object userId);
}
