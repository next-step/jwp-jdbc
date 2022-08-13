package core.jdbc;

import org.springframework.jdbc.core.PreparedStatementCallback;

import java.util.List;

public interface JdbcOperations {

    <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... values) throws DataAccessException;

    <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException;

    <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse);

    <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper);

    int update(String sql, Object... values) throws DataAccessException;

    int update(String sql, PreparedStatementSetter pss) throws DataAccessException;

    <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException;

    <T> T execute(StatementCallback<T> action) throws DataAccessException;
}
