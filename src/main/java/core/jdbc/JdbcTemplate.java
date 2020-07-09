package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    public static int executeUpdate(String sql, Object[] objects) throws SQLException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            setObjects(pstmt, objects);

            return pstmt.executeUpdate();
        }
    }

    public static <T> T executeQuery(String sql, Object[] objects, RowMapper<T> mapper) throws SQLException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            setObjects(pstmt, objects);


            try (ResultSet rs = pstmt.executeQuery()) {
                return mapper.mapping(rs);
            }
        }
    }

    private static void setObjects(PreparedStatement pstmt, Object[] objects) throws SQLException {
        if (objects == null) {
            return;
        }

        for (int i = 0; i < objects.length; i++) {
            pstmt.setObject(i + 1, objects[i]);
        }
    }
}
