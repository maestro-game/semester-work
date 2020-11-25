package repositories;

import lombok.AllArgsConstructor;
import models.Category;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    //language=SQL
    private static final String SQL_FIND_CHILD_CATEGORIES = "SELECT * FROM categories WHERE id >= ? AND id <= ? AND id % ? = 0";
    //language=SQL
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM categories WHERE id = ?";

    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Category> categoryRowMapper = row -> new Category(row.getLong(1), row.getString(2));

    @Override
    public List<Category> findChildCategories(Taxon taxon, Long id) {
        return jdbcTemplate.listQuery(SQL_FIND_CHILD_CATEGORIES, categoryRowMapper, id + taxon.getDistance(), id + taxon.getBitMask(), taxon.getDistance());
    }

    @Override
    public RowMapper<Category> getRowMapper() {
        return categoryRowMapper;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return jdbcTemplate.entityQuery(SQL_SELECT_BY_ID, categoryRowMapper, id);
    }

    @Override
    public void save(Category entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException();
    }
}
