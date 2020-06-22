package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author KingCjy
 */
public interface ResultSetExtractor<T> {
    T extractData(ResultSet rs) throws SQLException;
}
