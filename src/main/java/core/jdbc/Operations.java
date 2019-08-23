package core.jdbc;

import java.util.List;

public interface Operations {
    <T> T execute(String query, RowMapper<T> rowMapper, Object... objects);

    <T> List<T> execute(String query, RowMapper<T> rowMapper);

    void execute(String query, Object... objects);
}
