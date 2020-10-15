package repositories;

import models.Comment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentsRepositoryJdbcImpl implements CommentsRepository{
    //language=SQL
    private static final String SQL_FIND_ALL_BY_AUTHOR_ID = "SELECT * FROM comments WHERE author = ?";
    //language=SQL
    private static final String SQL_FIND_BY_ID = "SELECT * FROM comments WHERE id = ?";
    //language=SQL
    private static final String SQL_FIND_BY_POST_ID = "SELECT * FROM comments WHERE post = ?";
    //language=SQL
    private static final String SQL_DELETE_BY_ID = "DELETE FROM comments WHERE id = ?";

    DataSource dataSource;

    private final RowMapper<Comment> commentRowMapper = row -> new Comment(row.getLong(1),
            null,
            row.getTimestamp(3),
            null,
            null,
            row.getString(6));

    public CommentsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Comment> findAllByAuthorId(String authorId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_BY_AUTHOR_ID)) {
                statement.setString(1, authorId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Comment> comments = new ArrayList<>();
                    while (resultSet.next()) {
                        comments.add(commentRowMapper.mapRow(resultSet));
                    }
                    return comments;
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public List<Comment> findAllByPostId(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_BY_AUTHOR_ID)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Comment> comments = new ArrayList<>();
                    while (resultSet.next()) {
                        comments.add(commentRowMapper.mapRow(resultSet));
                    }
                    return comments;
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Optional<Comment> findById(Long aLong) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void save(Comment entity) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void update(Comment entity) {
        throw new UnsupportedOperationException("Empty Realisation");
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException("Empty Realisation");
    }
}
