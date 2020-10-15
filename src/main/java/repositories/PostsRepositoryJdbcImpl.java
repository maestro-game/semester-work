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
    //language=SQL
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM posts WHERE id = ?";

    DataSource dataSource;
    UsersRepository usersRepository;

    public PostsRepositoryJdbcImpl(DataSource dataSource, UsersRepository usersRepository) {
        this.dataSource = dataSource;
        this.usersRepository = usersRepository;
    }
    private final RowMapper<Post> postRowMapper = row -> new Post(row.getLong(1), null, row.getString(3), row.getString(4));
    private final RowMapper<Post> postWithAuthor = row -> new Post(row.getLong(1), usersRepository.findById(row.getString(2)).get(), row.getString(3), row.getString(4));

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
    public Optional<Post> findById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(postWithAuthor.mapRow(resultSet));
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
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
