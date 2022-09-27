package core.jdbc;

import org.springframework.dao.DataAccessException;

import java.util.List;

public interface JdbcTemplate {
    void update(String sql, PreparedStatementSetter preparedStatementSetter) throws DataAccessException;
    void update(String sql, Object... values) throws DataAccessException;
    PreparedStatementSetter createPreparedStatementSetter(Object... values);
    <T> T get(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws DataAccessException;
    <T> T get(String sql, RowMapper<T> rowMapper, Object... values) throws DataAccessException;
    <T> List<T> findAll(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws DataAccessException;
    <T> List<T> findAll(String sql, RowMapper<T> rowMapper, Object... values) throws DataAccessException;
}
