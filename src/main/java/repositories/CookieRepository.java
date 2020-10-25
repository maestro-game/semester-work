package repositories;

import javax.servlet.http.Cookie;
import java.util.List;

public interface CookieRepository extends CrudRepository<Cookie, Cookie> {
    List<String> findAllByUserId(String id);
}
