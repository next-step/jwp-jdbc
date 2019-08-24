package core.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper {
    <T> T mappedRow(ResultSet rs) throws SQLException;
}
