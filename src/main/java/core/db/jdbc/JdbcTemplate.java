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
        QueryExecutor<?, Integer> executor = (pstmt, mapper) -> pstmt.executeUpdate();
        return executor.execute(sql, null, args);
    }

    public int update(String sql, PreparedStatementSetter setter) {
        QueryExecutor<?, Integer> executor = (pstmt, mapper) -> {
            setter.setValues(pstmt);
            return pstmt.executeUpdate();
        };

        return executor.execute(sql, null);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        QueryExecutor<T, List<T>> executor = (pstmt, mapper) -> getListByMapper(mapper, pstmt);
        return executor.execute(sql, rowMapper, args);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        QueryExecutor<T, List<T>> executor = (pstmt, mapper) -> {
            setter.setValues(pstmt);
            return getListByMapper(mapper, pstmt);
        };
        return executor.execute(sql, rowMapper);
    }

    public <T> Optional<T> querySingle(String sql, RowMapper<T> rowMapper, Object... args) {
        return Optional.of(query(sql, rowMapper, args))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0));
    }

    public <T> Optional<T> querySingle(String sql, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        return Optional.of(query(sql, rowMapper, setter))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0));
    }

    private <T> List<T> getListByMapper(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<T> items = Lists.newArrayList();
            if (rs.next()) {
                T item = rowMapper.mapRow(rs);
                items.add(item);
            }
            return items;
        }
    }
}
