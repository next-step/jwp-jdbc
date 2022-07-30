package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
    public T mapRow(ResultSet resultSet) throws SQLException;
}
