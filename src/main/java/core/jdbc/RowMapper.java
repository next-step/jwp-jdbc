package core.jdbc;

import java.sql.ResultSet;

@FunctionalInterface
public interface RowMapper<T> {
    T mapping(ResultSet rs) throws Exception;
}
