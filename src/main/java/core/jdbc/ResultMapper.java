package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hspark on 2019-08-22.
 */
public interface ResultMapper<T> {
    T map(ResultSet rs) throws SQLException;
}
