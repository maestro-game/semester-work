package repositories;

import models.Comment;
import models.User;

import java.util.List;
import java.util.Optional;

public class CommentsRepositoryJdbcImpl implements CommentsRepository {
    //language=SQL
    private static final String SQL_FIND_PAGE_BY_AUTHOR_ID = "SELECT * FROM comments WHERE author = ? offset ? limit ?";
    //language=SQL
    private static final String SQL_FIND_BY_ID = "SELECT u.*, c.id commentId, c.timestamp, c.post, c.answers, c.text FROM comments c join users u on id = ? and u.id = c.author";
    //language=SQL
    private static final String SQL_FIND_PAGE_BY_POST_ID = "SELECT u.*, c.id commentId, c.timestamp, c.post, c.answers, c.text FROM comments c join users u on post = ? and u.id = c.author offset ? limit ?";
    //language=SQL
    private static final String SQL_DELETE_BY_ID = "DELETE FROM comments WHERE id = ?";
    //language=SQL
    private static final String SQL_SAFE = "INSERT INTO comments values (default, ?, ?, ?, ?, ?)";
    //language=SQL
    private static final String SQL_SAFE_RETURNING_ID = "INSERT INTO comments values (default, ?, ?, ?, ?, ?) returning id";
    //language=SQL
    private static final String SQL_UPDATE_TEXT = "UPDATE comments SET text = ? WHERE id = ?";

    private JdbcTemplate jdbcTemplate;

    public CommentsRepositoryJdbcImpl(JdbcTemplate jdbcTemplate, UsersRepository usersRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = usersRepository.getRowMapper();
    }

    private RowMapper<User> userRowMapper;
    private RowMapper<Comment> commentRowMapper = row -> new Comment(row.getLong(1),
            null,
            row.getTimestamp(3),
            null,
            null,
            row.getString(6));

    private final RowMapper<Comment> commentWithAuthor = row -> {
        long ansId = row.getLong("answers");
        return new Comment(row.getLong("commentId"),
                userRowMapper.mapRow(row),
                row.getTimestamp("timestamp"),
                null,
                ansId != 0 ? Comment.builder().id(ansId).build() : null,
                row.getString("text"));
    };

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
    public void updateText(Long id, String text) {
        jdbcTemplate.executeQuery(SQL_UPDATE_TEXT, text, id);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.executeQuery(SQL_DELETE_BY_ID, id);
    }
}
