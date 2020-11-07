package repositories;

import models.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long>{
    List<Category> findChildCategories(Taxon taxon, Long id);
    RowMapper<Category> getRowMapper();
}
