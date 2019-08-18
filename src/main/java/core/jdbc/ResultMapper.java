package core.jdbc;

import java.sql.SQLException;

public interface ResultMapper<T, R> {
    R resultMapping(T result) throws SQLException;
}