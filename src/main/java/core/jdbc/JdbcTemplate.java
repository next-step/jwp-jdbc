package core.jdbc;

import java.util.List;

public interface JdbcTemplate {
    void update(String sql, PreparedStatementSetter pstmt);
    void update(String sql, Object... values);
    PreparedStatementSetter createPreparedStatementSetter(Object... values);
    <T> T createForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmt);
    <T> T createForObject(String sql, RowMapper<T> rowMapper, Object... values);
    <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmt);
    <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... values);
}
