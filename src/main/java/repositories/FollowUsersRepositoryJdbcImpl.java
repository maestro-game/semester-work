package repositories;

import models.FollowUser;
import models.User;

import java.util.List;
import java.util.Optional;

public class FollowUsersRepositoryJdbcImpl implements FollowUsersRepository{
    JdbcTemplate jdbcTemplate;

    public FollowUsersRepositoryJdbcImpl(JdbcTemplate jdbcTemplate, UsersRepository usersRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = usersRepository.getRowMapper();
    }

    //language=SQL
    private static final String SQL_SAVE = "INSERT INTO \"followUsers\" values (?, ?)";
    //language=SQL
    private static final String SQL_DELETE = "DELETE FROM \"followUsers\" where \"user\" = ? and follow = ?";
    //language=SQL
    private static final String SQL_FIND_BY_USER_ID = "SELECT u.* FROM \"followUsers\" f JOIN users u on f.\"user\" = ? and u.id = f.follow";

    private final RowMapper<User> userRowMapper;

    @Override
    public List<User> findByUserId(String id) {
        return jdbcTemplate.listQuery(SQL_FIND_BY_USER_ID, userRowMapper, id);
    }

    @Override
    public Optional<FollowUser> findById(FollowUser followUser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(FollowUser followUser) {
        jdbcTemplate.executeQuery(SQL_SAVE, followUser.getUser(), followUser.getFollow());
    }

    @Override
    public void deleteById(FollowUser followUser) {
        jdbcTemplate.executeQuery(SQL_DELETE, followUser.getUser(), followUser.getFollow());
    }
}
