package core.db.jdbc;

import com.google.common.collect.Lists;
import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author : yusik
 * @date : 2019-08-17
 */
public class JdbcTemplate {

    public int refactorUpdate(String sql, Object... args) {
        QueryExecutor executor = (pstmt, callback) -> pstmt.executeUpdate();
        return (int) executor.execute(sql, null, args);
    }

    public <T> List<T> refactorQuery(String sql, QueryResultCallback<T> callback, Object... args) {
        QueryExecutor executor = (pstmt, cb) -> getItemsFromResultSet(cb, pstmt);
        return (List<T>) executor.execute(sql, callback, args);
    }

    public int update(String sql, Object... args) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcException(e.getMessage(), e);
        }
    }

    public <T> List<T> query(String sql, QueryResultCallback<T> callback, Object... args) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            return getItemsFromResultSet(callback, pstmt);
        } catch (SQLException e) {
            throw new JdbcException(e.getMessage(), e);
        }
    }

    private <T> List<T> getItemsFromResultSet(QueryResultCallback<T> callback, PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<T> items = Lists.newArrayList();

            if (rs.next()) {
                T item = callback.apply(rs);
                items.add(item);
            }
            return items;
        }
    }

    public <T> T querySingle(String sql, QueryResultCallback<T> callback, Object... args) {
        List<T> result = query(sql, callback, args);
        if (result.size() < 1) {
            return null;
        }
        return result.get(0);
    }

}
