package repositories;

import models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    //language=SQL
    private static final String SQL_SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    //language=SQL
    private static final String SQL_IS_EXIST_BY_ID_AND_PASS = "SELECT exists(SELECT 1 FROM users WHERE id = ? AND password = ?)";
    //language=SQL
    private static final String SQL_SAVE = "INSERT INTO users values (?, ?, ?, ?, ?, ?, ?, ?)";

    DataSource dataSource;

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private RowMapper<User> userRowMapper = row -> new User(row.getString(1),
            null,
            row.getString(3),
            row.getString(4),
            row.getString(5),
            row.getString(6),
            row.getDate(7),
            row.getString(8),
            null,
            null,
            null);

    @Override
    public Optional<User> findById(String id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_USER_BY_ID)) {
                statement.setString(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(userRowMapper.mapRow(resultSet));
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void save(User entity) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE)) {
                statement.setString(1, entity.getId());
                statement.setBytes(2, entity.getPassword());
                statement.setString(3, entity.getName());
                statement.setString(4, entity.getSurname());
                statement.setString(5, entity.getMiddleName());
                statement.setString(6, entity.getEmail());
                statement.setDate(7, entity.getBirth());
                statement.setString(8, entity.getAbout());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void update(User entity) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public boolean isExist(String id, byte[] hashPassword) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_IS_EXIST_BY_ID_AND_PASS)) {
                statement.setString(1, id);
                statement.setBytes(2, hashPassword);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBoolean(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return false;
    }
}
