package repositories;

import lombok.AllArgsConstructor;

import javax.servlet.http.Cookie;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CookieRepositoryJdbcImpl implements CookieRepository{
    private JdbcTemplate jdbcTemplate;

    //language=SQL
    private static String SQL_SAVE = "INSERT INTO cookies values (?, ?)";
    //language=SQL
    private static String SQL_FIND_ALL_NAMES_BY_USER_ID = "SELECT name FROM cookies WHERE \"user\" = ?";
    //language=SQL
    private static String SQL_DELETE = "DELETE FROM cookies WHERE name = ? AND \"user\" = ?";

    private static RowMapper<String> nameRowMapper = row -> row.getString(1);

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
    public List<String> findAllByUserId(String userId) {
        return jdbcTemplate.listQuery(SQL_FIND_ALL_NAMES_BY_USER_ID, nameRowMapper, userId);
    }
}
