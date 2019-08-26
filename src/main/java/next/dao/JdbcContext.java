package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class JdbcContext {

    void executeUpdate(final String sql) throws SQLException {
        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {

            setPreparedStatement(pstmt);

            pstmt.executeUpdate();
        }
    }

    <T> T executeQuery(final String sql, final RowMapper<T> rowMapper) throws SQLException {
        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {

            setPreparedStatement(pstmt);

            final ResultSet rs = pstmt.executeQuery();

            return rowMapper.mapping(rs);
        }
    }

    abstract void setPreparedStatement(PreparedStatement pstmt) throws SQLException;
}
