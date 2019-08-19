package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by youngjae.havi on 2019-08-18
 */
@FunctionalInterface
public interface SqlResultStrategy<T> {
    T resultSql(ResultSet rs) throws SQLException;
}
