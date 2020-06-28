package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {

    public void update(String sql, Object... args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                final Object value = args[i];

                if (value instanceof String) {
                    pstmt.setString(i + 1, (String) args[i]);
                }
                if (value instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) args[i]);
                }
            }

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

}
