package repositories;

import lombok.AllArgsConstructor;
import models.User;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.util.Optional;

@AllArgsConstructor
public class UsersRepositoryJdbcImpl implements UsersRepository {
    //language=SQL
    private static final String SQL_SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    //language=SQL
    private static final String SQL_SAVE = "INSERT INTO users values (?, ?, ?, ?, ?, ?, ?, ?)";

    private JdbcTemplate jdbcTemplate;

    private static RowMapper<User> userRowMapper = row -> new User(row.getString(1),
            null,
            row.getString(3),
            row.getString(4),
            row.getString(5),
            Path.of(row.getString(6)),
            row.getString(7),
            row.getDate(8),
            row.getString(9),
            null,
            null,
            null);

    @Override
    public Optional<User> findById(String id) {
        return jdbcTemplate.entityQuery(SQL_SELECT_USER_BY_ID, userRowMapper, id);
    }

    @Override
    public void save(User entity) {
        jdbcTemplate.executeQuery(SQL_SAVE,
                entity.getId(),
                entity.getPassword(),
                entity.getName(),
                entity.getSurname(),
                entity.getMiddleName(),
                entity.getEmail(),
                entity.getBirth(),
                entity.getAbout());
    }

    @Override
    public void update(User entity) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException("Empty Realisation");
    }
}
