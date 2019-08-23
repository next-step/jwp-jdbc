package core.jdbc;

import java.util.List;

public interface JdbcOperations {

    void execute(String sql, Object... parameters);

    <T> List<T> queryForList(String sql, ResultSetExtractor<T> requestType, Object... parameters);

    <T> T queryForObject(String sql, ResultSetExtractor<T> resultSetExtractor, Object... parameters);
}