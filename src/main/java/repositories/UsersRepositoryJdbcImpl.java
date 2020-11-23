package repositories;

import lombok.AllArgsConstructor;
import models.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UsersRepositoryJdbcImpl implements UsersRepository {
    //language=SQL
    private static final String SQL_SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    //language=SQL
    private static final String SQL_SAVE = "INSERT INTO users values (?, ?, ?, ?, NULL, ?)";
    //language=SQL
    private static final String SQL_UPDATE_FIELD = "UPDATE users SET ? = ? WHERE id = ?";
    //language=SQL
    private static final String SQL_SEARCH_BY_ID = "SELECT * FROM users WHERE id like ? order by id offset ? limit ?";

    private JdbcTemplate jdbcTemplate;
    private ImageRepository imageRepository;

    private final RowMapper<User> userRowMapper = row -> {
        String id = row.getString(1);
        return new User(id,
                row.getString(2),
                row.getString(3),
                row.getString(4),
                row.getString(5),
                row.getString(6),
                row.getDate(7),
                row.getString(8),
                imageRepository.pathForUser(id, row.getString(9)),
                null,
                null,
                null);
    };

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
                entity.getEmail());
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void updateField(String id, String field, String data) {
        jdbcTemplate.executeQuery(SQL_UPDATE_FIELD, field, data, id);
    }

    @Override
    public List<User> searchById(String id, int offset, int limit) {
        return jdbcTemplate.listQuery(SQL_SEARCH_BY_ID, userRowMapper, "%" + id + "%");
    }

    @Override
    public RowMapper<User> getRowMapper() {
        return userRowMapper;
    }
}
