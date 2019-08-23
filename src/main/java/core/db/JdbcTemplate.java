package core.db;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {

    public void executeUpdate(String sql, PreparedStatementSetter psmtSetter) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            psmtSetter.setPreparedStatement(pstmt);
            pstmt.executeUpdate();
        }
    }

    public <T> T executeQuery(String sql, RowMapper rowMapper, Object... params) throws SQLException {
        return executeQuery(sql, pstmt -> {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }, rowMapper);
    }

    public <T> T executeQuery(String sql, PreparedStatementSetter pstmtSetter, RowMapper rowMapper) throws SQLException {

        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmtSetter.setPreparedStatement(pstmt);
            rs = pstmt.executeQuery();

            T result = null;
            if (rs.next()) {
                result = rowMapper.mappedRow(rs);
            }
            return result;
        } finally {
            close(rs);
        }
    }

    private void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

}
