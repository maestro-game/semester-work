package repositories;

import lombok.AllArgsConstructor;
import models.Comment;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CommentsRepositoryJdbcImpl implements CommentsRepository{
    //language=SQL
    private static final String SQL_FIND_PAGE_BY_AUTHOR_ID = "SELECT * FROM comments WHERE author = ? offset ? limit ?";
    //language=SQL
    private static final String SQL_FIND_BY_ID = "SELECT * FROM comments WHERE id = ?";
    //language=SQL
    private static final String SQL_FIND_PAGE_BY_POST_ID = "SELECT * FROM comments WHERE post = ? ORDER BY timestamp offset ? limit ?";
    //language=SQL
    private static final String SQL_DELETE_BY_ID = "DELETE FROM comments WHERE id = ?";
    //language=SQL
    private static final String SQL_SAFE = "INSERT INTO comments values (default, ?, ?, ?, ?, ?)";
    //language=SQL
    private static final String SQL_SAFE_RETURNING_ID = "INSERT INTO comments values (default, ?, ?, ?, ?, ?) returning id";
    //language=SQL
    private static final String SQL_UPDATE_TEXT = "UPDATE comments SET text = ? WHERE id = ?";

    private JdbcTemplate jdbcTemplate;
    UsersRepository usersRepository;

    private final RowMapper<Comment> commentRowMapper = row -> new Comment(row.getLong(1),
            null,
            row.getTimestamp(3),
            null,
            null,
            row.getString(6));
    private final RowMapper<Comment> commentWithAuthor = row -> new Comment(row.getLong(1),
            usersRepository.findById(row.getString(2)).get(),
            row.getTimestamp(3),
            null,
            findById(row.getLong(5)).orElse(null),
            row.getString(6));
    private final RowMapper<Long> longRowMapper = row -> row.getLong(1);

    @Override
    public List<Comment> findPageByAuthorId(String authorId, int limit, int offset) {
        return jdbcTemplate.listQuery(SQL_FIND_PAGE_BY_AUTHOR_ID, commentRowMapper, authorId, limit, offset);
    }

    @Override
    public List<Comment> findPageByPostId(Long id, int limit, int offset) {
        return jdbcTemplate.listQuery(SQL_FIND_PAGE_BY_POST_ID, commentWithAuthor, id, limit, offset);
    }

    @Override
    public Long saveReturningId(Comment comment) {
        return jdbcTemplate.simpleQuery(SQL_SAFE_RETURNING_ID,
                longRowMapper,
                comment.getAuthor().getId(),
                comment.getTimestamp(),
                comment.getPost().getId(),
                comment.getAnswerTo() != null ? comment.getAnswerTo().getId() : null,
                comment.getText());
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return jdbcTemplate.entityQuery(SQL_FIND_BY_ID, commentWithAuthor, id);
    }

    @Override
    public void save(Comment comment) {
        jdbcTemplate.executeQuery(SQL_SAFE,
                comment.getAuthor().getId(),
                comment.getTimestamp(),
                comment.getPost().getId(),
                comment.getAnswerTo() != null ? comment.getAnswerTo().getId() : null,
                comment.getText());
    }

    @Override
    public void update(Comment entity) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void updateText(Long id, String text) {
        jdbcTemplate.executeQuery(SQL_UPDATE_TEXT, text, id);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.executeQuery(SQL_DELETE_BY_ID, id);
    }
}
