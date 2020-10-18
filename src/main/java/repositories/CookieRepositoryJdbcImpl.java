package repositories;

import lombok.AllArgsConstructor;
import models.Post;

import javax.servlet.http.Cookie;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CookieRepositoryJdbcImpl implements CookieRepository{
    DataSource dataSource;

    //language=SQL
    private static String SQL_SAVE = "INSERT INTO cookies values (?, ?)";
    //language=SQL
    private static String SQL_FIND_ALL_BY_USER_ID = "SELECT * FROM cookies WHERE \"user\" = ?";

    private static RowMapper<Cookie> cookieRowMapper = row -> new Cookie(row.getString(1), row.getString(2));

    @Override
    public Optional<Cookie> findById(String s) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void save(Cookie cookie) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE)) {
                statement.setString(1, cookie.getName());
                statement.setString(2, cookie.getValue());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void update(Cookie entity) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public List<Cookie> findAllByUserId(String userId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_BY_USER_ID)) {
                statement.setString(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Cookie> cookies = new LinkedList<>();
                    while (resultSet.next()) {
                        cookies.add(cookieRowMapper.mapRow(resultSet));
                    }
                    return cookies;
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
