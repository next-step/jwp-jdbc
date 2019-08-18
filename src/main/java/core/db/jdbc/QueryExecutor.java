package core.db.jdbc;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author : yusik
 * @date : 2019-08-17
 */
public interface QueryExecutor<T, R> {

    default R execute(String sql, RowMapper<T> rowMapper, Object... args) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            return internalExecute(pstmt, rowMapper);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    R internalExecute(PreparedStatement pstmt, RowMapper<T> rowMapper) throws SQLException;
}