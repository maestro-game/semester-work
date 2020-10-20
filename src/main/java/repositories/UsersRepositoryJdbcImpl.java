package repositories;

import models.User;

import javax.sql.DataSource;
import java.util.Optional;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    //language=SQL
    private static final String SQL_SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    //language=SQL
    private static final String SQL_IS_EXIST_BY_ID_AND_PASS = "SELECT exists(SELECT 1 FROM users WHERE id = ? AND password = ?)";
    //language=SQL
    private static final String SQL_SAVE = "INSERT INTO users values (?, ?, ?, ?, ?, ?, ?, ?)";

    JdbcTemplate jdbcTemplate;

    private RowMapper<Boolean> isExistRowMapper = row -> row.getBoolean(1);

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

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplateImpl(dataSource);
    }

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

    @Override
    public boolean isExist(String id, byte[] hashPassword) {
        return jdbcTemplate.simpleQuery(SQL_IS_EXIST_BY_ID_AND_PASS, isExistRowMapper, id, hashPassword);
    }
}
