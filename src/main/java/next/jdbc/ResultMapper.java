package next.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultMapper<T> {

    T mapping(final ResultSet resultSet) throws SQLException;
}
