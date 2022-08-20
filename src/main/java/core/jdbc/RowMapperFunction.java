package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapperFunction<T> {

    T apply(ResultSet resultSet) throws SQLException;
}
