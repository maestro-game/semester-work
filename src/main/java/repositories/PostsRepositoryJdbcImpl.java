package repositories;

import models.Post;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class PostsRepositoryJdbcImpl implements PostsRepository {
    //language=SQL
    private static final String SQL_SELECT_BY_AUTHOR_ID = "SELECT * FROM posts WHERE author = ?";
    //language=SQL
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM posts WHERE id = ?";

    JdbcTemplate jdbcTemplate;
    UsersRepository usersRepository;

    public PostsRepositoryJdbcImpl(DataSource dataSource, UsersRepository usersRepository) {
        this.jdbcTemplate = new JdbcTemplateImpl(dataSource);
        this.usersRepository = usersRepository;
    }
    private final RowMapper<Post> postRowMapper = row -> new Post(row.getLong(1),
            null,
            row.getTimestamp(3),
            row.getString(4),
            row.getString(5));
    private final RowMapper<Post> postWithAuthor = row -> new Post(row.getLong(1),
            usersRepository.findById(row.getString(2)).get(),
            row.getTimestamp(3),
            row.getString(4),
            row.getString(5));

    @Override
    public List<Post> findAllByAuthorId(String authorId) {
        return jdbcTemplate.listQuery(SQL_SELECT_BY_AUTHOR_ID, postRowMapper, authorId);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return jdbcTemplate.entityQuery(SQL_SELECT_BY_ID, postWithAuthor, id);
    }

    @Override
    public void save(Post entity) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void update(Post entity) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException("Empty Realisation");
    }
}
