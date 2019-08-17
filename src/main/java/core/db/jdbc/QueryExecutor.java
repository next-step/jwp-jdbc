package core.db.jdbc;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author : yusik
 * @date : 2019-08-17
 */
public interface QueryExecutor {

    default Object execute(String sql, QueryResultCallback<?> callback, Object... args) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            return internalExecute(pstmt, callback);
        } catch (SQLException e) {
            throw new JdbcException(e.getMessage(), e);
        }
    }

    Object internalExecute(PreparedStatement pstmt, QueryResultCallback<?> callback) throws SQLException;
}