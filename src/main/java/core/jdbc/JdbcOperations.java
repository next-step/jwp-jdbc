package core.jdbc;

import java.util.List;
import java.util.Optional;

public interface JdbcOperations {

    void execute(String sql, Object... parameters);

    <T> List<T> queryForList(String sql, ResultSetExtractor<T> requestType);

    <T> Optional<T> queryForObject(String sql, ResultSetExtractor<T> resultSetExtractor, Object... parameters);
}
