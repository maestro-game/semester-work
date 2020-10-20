package utils;

import lombok.AllArgsConstructor;
import repositories.CookieRepository;

import javax.servlet.http.Cookie;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class CookieManagerImpl implements CookieManager {
    CookieRepository cookieRepository;

    @Override
    public Cookie assign(Object userId) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[(int) (Math.random() * 22)];
        random.nextBytes(bytes);
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            int a = (aByte + 128) % 62;
            if (a > 25) {
                if (a > 51) {
                    builder.append(a - 52);
                } else {
                    builder.append((char) (39 + a));
                }
            } else {
                builder.append((char) (97 + a));
            }
        }
        Cookie cookie = new Cookie(builder.toString(), (String) userId);
        try {
            cookieRepository.save(cookie);
        } catch (IllegalArgumentException e) {
            SQLException sql = (SQLException) e.getCause();
            if (!"23505".equals(sql.getSQLState())) {
                throw new IllegalArgumentException(e);
            }
        }
        cookie.setValue(String.valueOf(Math.random() * 10000));
        return cookie;
    }

    @Override
    public void remove(Cookie cookie) {
        cookieRepository.deleteById(cookie);
    }

    @Override
    public List<Cookie> getAllByUserId(Object userId) {
        return cookieRepository.findAllByUserId((String) userId);
    }
}
