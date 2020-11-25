package repositories;

import lombok.AllArgsConstructor;
import models.Post;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PostsRepositoryJdbcImpl implements PostsRepository {
    //language=SQL
    private static final String SQL_SELECT_PAGE_BY_AUTHOR_ID = "SELECT * FROM posts WHERE author = ? offset ? limit ?";
    //language=SQL
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM posts WHERE id = ?";
    //language=SQL
    private static final String SQL_UPDATE_TEXT = "UPDATE posts SET description = ? WHERE id = ?";
    //language=SQL
    private static final String SQL_SAVE = "INSERT INTO posts values (default, ?, ?, ?, ?, ?) returning id";
    //language=SQL
    private static final String SQL_SELECT_PAGE_BY_CATEGORY = "SELECT * FROM posts WHERE specie > ? AND specie <= ? ORDER BY timestamp offset ? limit ?";

    private JdbcTemplate jdbcTemplate;
    UsersRepository usersRepository;
    CategoryRepository categoryRepository;

    private final RowMapper<Post> postRowMapper = row -> new Post(row.getLong(1),
            null,
            row.getTimestamp(3),
            row.getString(4),
            row.getString(5),
            categoryRepository.findById(row.getLong(6)).get());
    private final RowMapper<Post> postWithAuthor = row -> new Post(row.getLong(1),
            usersRepository.findById(row.getString(2)).get(),
            row.getTimestamp(3),
            row.getString(4),
            row.getString(5),
            categoryRepository.findById(row.getLong(6)).get());
    private final RowMapper<Long> postReturningId = row -> row.getLong(1);



    @Override
    public List<Post> findPageByAuthorId(String authorId, int offset, int limit) {
        return jdbcTemplate.listQuery(SQL_SELECT_PAGE_BY_AUTHOR_ID, postRowMapper, authorId, offset, limit);
    }

    @Override
    public List<Post> findPageByCategory(Taxon taxon, Long id, int offset, int limit) {
        return jdbcTemplate.listQuery(SQL_SELECT_PAGE_BY_CATEGORY, postRowMapper, id, id + taxon.getBitMask(), offset, limit);
    }

    @Override
    public void updateDescription(Long id, String text) {
        jdbcTemplate.executeQuery(SQL_UPDATE_TEXT, id, text);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return jdbcTemplate.entityQuery(SQL_SELECT_BY_ID, postWithAuthor, id);
    }

    @Override
    public void save(Post post) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public long saveReturningId(Post post) {
        return jdbcTemplate.simpleQuery(SQL_SAVE, postReturningId, post.getAuthor().getId(), post.getTimestamp(), post.getDescription(), post.getImage(), post.getSpecie());
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException("Empty Realisation");
    }
}
