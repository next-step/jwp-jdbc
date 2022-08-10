package core.jdbc;

import org.springframework.dao.DataAccessException;

import java.util.List;

public interface JdbcTemplate {
    void update(String sql, PreparedStatementSetter pstmt) throws DataAccessException;
    void update(String sql, Object... values) throws DataAccessException;
    PreparedStatementSetter createPreparedStatementSetter(Object... values);
    <T> T createForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmt) throws DataAccessException;
    <T> T createForObject(String sql, RowMapper<T> rowMapper, Object... values) throws DataAccessException;
    <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmt) throws DataAccessException;
    <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... values) throws DataAccessException;
}
