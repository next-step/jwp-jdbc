package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    public static int executeUpdate(String sql, QuerySetter sqlExecute) throws SQLException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            sqlExecute.queryValues(pstmt);

            return pstmt.executeUpdate();
        }
    }

    public static <T> T executeQuery(String sql, QuerySetter sqlExecute, RowMapper<T> mapper) throws SQLException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            sqlExecute.queryValues(pstmt);


            try (ResultSet rs = pstmt.executeQuery()) {
                return mapper.mapping(rs);
            }
        }
    }
}
