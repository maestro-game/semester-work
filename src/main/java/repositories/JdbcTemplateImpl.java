package repositories;

import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class JdbcTemplateImpl implements JdbcTemplate {
    DataSource dataSource;

    private interface Processor {
        Object entityProcess(RowMapper rowMapper, ResultSet resultSet) throws SQLException;
    }

    private final Processor optionalProcessor = (RowMapper rowMapper, ResultSet resultSet) -> {
        if (resultSet.next()) {
            return Optional.of(rowMapper.mapRow(resultSet));
        } else {
            return Optional.empty();
        }
    };

    private final Processor listProcessor = (RowMapper rowMapper, ResultSet resultSet) -> {
        List<Object> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(rowMapper.mapRow(resultSet));
        }
        return list;
    };

    private final Processor simpleProcessor = (RowMapper rowMapper, ResultSet resultSet) -> {
        resultSet.next();
        return rowMapper.mapRow(resultSet);
    };

    private void simpleQuery(String sql, Object[] args) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                if (args != null) {
                    int pos = 1;
                    for (Object arg : args) {
                        statement.setObject(pos++, arg);
                    }
                }
                statement.execute();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Object queryProcessor(Processor processor, String sql, RowMapper rowMapper, Object[] args) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                if (args != null) {
                    int pos = 1;
                    for (Object arg : args) {
                        statement.setObject(pos++, arg);
                    }
                }
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet == null) throw new SQLException("Empty result");
                    return processor.entityProcess(rowMapper, resultSet);
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public <T> T simpleQuery(String sql, RowMapper<T> rowMapper, Object... args) {
        return (T) queryProcessor(simpleProcessor, sql, rowMapper, args);
    }

    @Override
    public <T> List<T> listQuery(String sql, RowMapper<T> rowMapper, Object... args) {
        return (List<T>) queryProcessor(listProcessor, sql, rowMapper, args);
    }

    @Override
    public <T> Optional<T> entityQuery(String sql, RowMapper<T> rowMapper, Object... args) {
        return (Optional<T>) queryProcessor(optionalProcessor, sql, rowMapper, args);
    }

    @Override
    public void executeQuery(String sql, Object... args) {
        simpleQuery(sql, args);
    }
}
