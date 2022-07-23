package next.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
interface PreparedStatementSetter {
    void set(PreparedStatement pstmt) throws SQLException;
}
