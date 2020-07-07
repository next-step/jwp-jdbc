package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created By kjs4395 on 2020-07-06
 */
public class JdbcTemplate {
    public void insertOrUpdate(String sql, BindPrepareStatement bindPrepareStatement) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            bindPrepareStatement.setPrepareStatement(pstmt);

            pstmt.executeUpdate();
        } finally {
            this.closeConnection(con,pstmt);
        }
    }

    public void insertOrUpdate(String sql, Object...values) throws SQLException {
        BindPrepareStatement bindPrepareStatement = pstmt -> {
            for(int i=0; i<values.length; i++) {
                pstmt.setObject(i+1,values[i]);
            }
        };
        insertOrUpdate(sql, bindPrepareStatement);
    }

    private void closeConnection(Connection con, PreparedStatement pstmt) throws SQLException {
        if (pstmt != null) {
            pstmt.close();
        }

        if (con != null) {
            con.close();
        }
    }
}
