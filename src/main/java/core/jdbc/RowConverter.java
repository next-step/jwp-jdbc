package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowConverter<T> {
    T convert(final ResultSet resultSet) throws SQLException;
}
