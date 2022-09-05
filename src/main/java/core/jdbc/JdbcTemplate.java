package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private static class JdbcTemplateHolder {
        private static final JdbcTemplate INSTANCE = new JdbcTemplate();
    }

    public static JdbcTemplate getInstance() {
        return JdbcTemplateHolder.INSTANCE;
    }

    public void update(String sql, Object... parameters) {
        update(sql, createPreparedStatementSetter(parameters));
    }

    public void update(String sql, PreparedStatementSetter pss) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
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

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return query(sql, rowMapper, createPreparedStatementSetter(parameters));
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {
            pss.setValues(pstmt);
            rs = pstmt.executeQuery();

            List<T> result = new ArrayList<T>();
            if (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... parameters) {
        return pstmt -> {
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }
        };
    }
}
