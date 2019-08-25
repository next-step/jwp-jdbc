package core.db;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
    <T> T mappedRow(ResultSet rs) throws SQLException;
}
