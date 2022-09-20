package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void update(String sql, Object... values) {
        PreparedStatementSetter pss = createPreparedStatementSetter(values);
        update(sql, pss);
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... values) {
        return ps -> {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
        };
    }

    public void update(String sql, PreparedStatementSetter pss) {
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
            pss.setValues(ps);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... values) {
        return query(sql, rowMapper, createPreparedStatementSetter(values));
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            pss.setValues(ps);
            ResultSet rs = ps.executeQuery();

            List<T> result = new ArrayList<>();
            if (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return queryForObject(sql, rowMapper, createPreparedStatementSetter(parameters));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        List<T> result = query(sql, rowMapper, pss);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
}
