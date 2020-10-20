package repositories;

import java.util.List;
import java.util.Optional;

public interface JdbcTemplate {
    <T> T simpleQuery(String sql, RowMapper<T> rowMapper, Object ... args);
    <T> List<T> listQuery(String sql, RowMapper<T> rowMapper, Object ... args);
    <T> Optional<T> entityQuery(String sql, RowMapper<T> rowMapper, Object ... args);
    void executeQuery(String sql, Object ... args);
}
