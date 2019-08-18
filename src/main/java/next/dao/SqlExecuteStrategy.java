package next.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by youngjae.havi on 2019-08-18
 */
@FunctionalInterface
public interface SqlExecuteStrategy {
    void executeSql(PreparedStatement ps) throws SQLException;
}
