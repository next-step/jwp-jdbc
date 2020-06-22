package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author KingCjy
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
