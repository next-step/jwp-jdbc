package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapperV2<T> {

    T map(ResultSet resultSet) throws SQLException;

}
