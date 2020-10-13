package repositories;

import models.Post;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostsRepositoryJdbcImpl implements PostsRepository {
    //language=SQL
    private static final String SQL_SELECT_BY_AUTHOR_ID = "SELECT * FROM posts WHERE author = ?";

    DataSource dataSource;

    public PostsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    //TODO fix work with image
    private final RowMapper<Post> postRowMapper = row -> new Post(row.getLong(1), null, row.getString(3), row.getString(4));

    @Override
    public List<Post> findAllByAuthorId(String authorId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_AUTHOR_ID)) {
                statement.setString(1, authorId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Post> posts = new ArrayList<>();
                    while (resultSet.next()) {
                        posts.add(postRowMapper.mapRow(resultSet));
                    }
                    return posts;
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Optional<Post> findById(Long aLong) {
        throw new UnsupportedOperationException("Empty Realisation");
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
