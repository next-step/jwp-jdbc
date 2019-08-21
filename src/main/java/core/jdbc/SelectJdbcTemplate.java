package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SelectJdbcTemplate {
    public Object select(String sql) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            setValues(pstmt);

            rs = pstmt.executeQuery();
            return mapRow(rs);
        } finally {
            close(rs);
            close(con, pstmt);
        }
    }

    public abstract Object mapRow(ResultSet rs) throws SQLException;

    public abstract void setValues(PreparedStatement pstmt) throws SQLException;

    private void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    private void close(Connection con, PreparedStatement pstmt) throws SQLException {
        if (pstmt != null) {
            pstmt.close();
        }

        if (con != null) {
            con.close();
        }
    }
}
