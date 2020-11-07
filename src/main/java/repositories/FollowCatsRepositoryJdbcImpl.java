package repositories;

import models.Category;
import models.FollowCat;

import java.util.List;
import java.util.Optional;

public class FollowCatsRepositoryJdbcImpl implements FollowCatsRepository{
    JdbcTemplate jdbcTemplate;

    public FollowCatsRepositoryJdbcImpl(JdbcTemplate jdbcTemplate, CategoryRepository categoryRepository) {
        this.jdbcTemplate = jdbcTemplate;
        categoryRowMapper = categoryRepository.getRowMapper();
    }

    //language=SQL
    private static final String SQL_FIND_BY_USER_ID = "SELECT c.* FROM \"followCats\" f JOIN categories c on f.\"user\" = ? and c.id = f.category";
    //language=SQL
    private static final String SQL_SAVE = "INSERT INTO \"followCats\" values (?, ?)";
    //language=SQL
    private static final String SQL_DELETE = "delete from \"followCats\" where \"user\" = ? and category = ?";

    private RowMapper<Category> categoryRowMapper;

    @Override
    public Optional<FollowCat> findById(FollowCat followCat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(FollowCat followCat) {
        jdbcTemplate.executeQuery(SQL_SAVE, followCat.getUser(), followCat.getCategory());
    }

    @Override
    public void deleteById(FollowCat followCat) {
        jdbcTemplate.executeQuery(SQL_DELETE, followCat.getUser(), followCat.getCategory());
    }

    @Override
    public List<Category> findByUserId(String id) {
        return jdbcTemplate.listQuery(SQL_FIND_BY_USER_ID, categoryRowMapper, id);
    }
}
