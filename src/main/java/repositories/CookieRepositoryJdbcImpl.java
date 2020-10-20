package repositories;

import javax.servlet.http.Cookie;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class CookieRepositoryJdbcImpl implements CookieRepository{
    JdbcTemplate jdbcTemplate;

    //language=SQL
    private static String SQL_SAVE = "INSERT INTO cookies values (?, ?)";
    //language=SQL
    private static String SQL_FIND_ALL_BY_USER_ID = "SELECT * FROM cookies WHERE \"user\" = ?";
    //language=SQL
    private static String SQL_DELETE = "DELETE FROM cookies WHERE name = ? AND \"user\" = ?";

    private static RowMapper<Cookie> cookieRowMapper = row -> new Cookie(row.getString(1), row.getString(2));

    public CookieRepositoryJdbcImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplateImpl(dataSource);
    }

    @Override
    public Optional<Cookie> findById(Cookie cookie) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void save(Cookie cookie) {
        jdbcTemplate.executeQuery(SQL_SAVE, cookie.getName(), cookie.getValue());
    }

    @Override
    public void update(Cookie entity) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void deleteById(Cookie cookie) {
        jdbcTemplate.executeQuery(SQL_DELETE, cookie.getName(), cookie.getValue());
    }

    @Override
    public List<Cookie> findAllByUserId(String userId) {
        return jdbcTemplate.listQuery(SQL_FIND_ALL_BY_USER_ID, cookieRowMapper, userId);
    }
}
