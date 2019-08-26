package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract class JdbcContext {

    void executeUpdate(final String sql) throws SQLException {
        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {

            setPreparedStatement(pstmt);

            pstmt.executeUpdate();
        }
    }

    abstract void setPreparedStatement(PreparedStatement pstmt) throws SQLException;
}
