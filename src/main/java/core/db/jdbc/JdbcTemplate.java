package core.db.jdbc;

import com.google.common.collect.Lists;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author : yusik
 * @date : 2019-08-17
 */
public class JdbcTemplate {

    public int update(String sql, Object... args) {
        QueryExecutor<?, Integer> executor = (pstmt, callback) -> pstmt.executeUpdate();
        return executor.execute(sql, null, args);
    }

    public <T> List<T> query(String sql, QueryResultCallback<T> callback, Object... args) {
        QueryExecutor<T, List<T>> executor = (pstmt, cb) -> getItemsFromResultSet(cb, pstmt);
        return executor.execute(sql, callback, args);
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

    public <T> Optional<T> querySingle(String sql, QueryResultCallback<T> callback, Object... args) {
        return Optional.of(query(sql, callback, args))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0));
    }

}
