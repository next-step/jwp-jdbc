package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate {
    public static int executeUpdate(String sql, Object... objects) throws SQLException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            setObjects(pstmt, objects);

            return pstmt.executeUpdate();
        }
    }

    public static int executeUpdate(String sql, List<Object> list) throws SQLException {
        return executeUpdate(sql, list.toArray());
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

    public static <T> T executeQuery(String sql, List<Object> list, RowMapper<T> mapper) throws SQLException {
        return executeQuery(sql, list.toArray(), mapper);
    }

    public static <T> T executeQuery(String sql, RowMapper<T> mapper) throws SQLException {
        return executeQuery(sql, new Object[0], mapper);
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
