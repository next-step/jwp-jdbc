package core.db.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author : yusik
 * @date : 2019-08-18
 */
@FunctionalInterface
public interface PreparedStatementSetter {
    void setValues(PreparedStatement pstmt) throws SQLException;
}
