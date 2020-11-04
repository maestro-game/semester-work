package repositories;

import lombok.AllArgsConstructor;
import models.Like;
import models.Post;
import models.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class LikesRepositoryJdbcImpl implements LikesRepository {
    private JdbcTemplate jdbcTemplate;

    //language=SQL
    private final String SQL_IS_LIKED = "SELECT EXISTS(SELECT 1 FROM likes WHERE \"user\" = ? AND post = ?)";
    //language=SQL
    private final String SQL_FIND_ALL_BY_USER_ID = "SELECT * FROM likes WHERE \"user\" = ?";
    //language=SQL
    private final String SQL_FIND_ALL_BY_POST_ID = "SELECT * FROM likes WHERE post = ?";
    //language=SQL
    private final String SQL_SAVE = "INSERT INTO likes values (?, ?)";
    //language=SQL
    private final String SQL_DELETE = "DELETE FROM likes WHERE \"user\" = ? AND post = ?";
    //language=SQL
    private final String SQL_COUNT_BY_POST_ID = "SELECT count(1) FROM likes WHERE post = ?";

    private static RowMapper<Like> likeRowMapper = row -> new Like(User.builder().id(row.getString(1)).build(),
            Post.builder().id(row.getLong(2)).build());

    private static RowMapper<Boolean> booleanRowMapper = row -> row.getBoolean(1);

    private static RowMapper<Integer> counterRowMapper = row -> row.getInt(1);

    @Override
    public boolean isLiked(String userId, Long postId) {
        return jdbcTemplate.simpleQuery(SQL_IS_LIKED,  booleanRowMapper, userId, postId);
    }

    @Override
    public List<Like> findAllByUserId(String userId) {
        return jdbcTemplate.listQuery(SQL_FIND_ALL_BY_USER_ID, likeRowMapper, userId);
    }

    @Override
    public List<Like> findALlByPostId(Long postId) {
        return jdbcTemplate.listQuery(SQL_FIND_ALL_BY_POST_ID, likeRowMapper, postId);
    }

    @Override
    public Integer countByPostId(Long postId) {
        return jdbcTemplate.simpleQuery(SQL_COUNT_BY_POST_ID, counterRowMapper, postId);
    }

    @Override
    public Optional<Like> findById(Like like) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(Like like) {
        jdbcTemplate.executeQuery(SQL_SAVE, like.getUser().getId(), like.getPost().getId());
    }

    @Override
    public void update(Like entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Like like) {
        jdbcTemplate.executeQuery(SQL_DELETE, like.getUser().getId(), like.getPost().getId());
    }
}
