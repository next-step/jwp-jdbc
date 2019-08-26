package next.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ThrowableRowMapper<T> {
    T mapRow(ResultSet rs) throws SQLException;
}

