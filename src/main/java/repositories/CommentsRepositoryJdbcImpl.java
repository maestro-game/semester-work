package repositories;

import models.Comment;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class CommentsRepositoryJdbcImpl implements CommentsRepository{
    //language=SQL
    private static final String SQL_FIND_ALL_BY_AUTHOR_ID = "SELECT * FROM comments WHERE author = ?";
    //language=SQL
    private static final String SQL_FIND_BY_ID = "SELECT * FROM comments WHERE id = ?";
    //language=SQL
    private static final String SQL_FIND_ALL_BY_POST_ID = "SELECT * FROM comments WHERE post = ?";
    //language=SQL
    private static final String SQL_DELETE_BY_ID = "DELETE FROM comments WHERE id = ?";
    //language=SQL
    private static final String SQL_SAFE = "INSERT INTO comments values (default, ?, ?, ?, ?, ?)";

    JdbcTemplate jdbcTemplate;
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
            null,
            row.getString(6));

    public CommentsRepositoryJdbcImpl(DataSource dataSource, UsersRepository usersRepository) {
        this.jdbcTemplate = new JdbcTemplateImpl(dataSource);
        this.usersRepository = usersRepository;
    }

    @Override
    public List<Comment> findAllByAuthorId(String authorId) {
        return jdbcTemplate.listQuery(SQL_FIND_ALL_BY_AUTHOR_ID, commentRowMapper, authorId);
    }

    @Override
    public List<Comment> findAllByPostId(Long id) {
        return jdbcTemplate.listQuery(SQL_FIND_ALL_BY_POST_ID, commentWithAuthor, id);
    }

    @Override
    public Optional<Comment> findById(Long aLong) {
        throw new UnsupportedOperationException("Empty Realisation");
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
    public void deleteById(Long id) {
        jdbcTemplate.executeQuery(SQL_DELETE_BY_ID, id);
    }
}
