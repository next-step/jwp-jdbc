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
            throw new DataAccessException(String.format("DB update sql 수행에 실패했습니다. sql = %s", sql), e);
        }
    }

    public <T> T queryForObject(String sql, Class<T> type, Object... parameters) {
        return queryForObject(sql, type, createPreparedStatementSetter(parameters));
    }

    public <T> T queryForObject(String sql, Class<T> type, PreparedStatementSetter pss) {
        List<T> result = query(sql, type, pss);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    public <T> List<T> query(String sql, Class<T> type, Object... parameters) {
        return query(sql, type, createPreparedStatementSetter(parameters));
    }

    public <T> List<T> query(String sql, Class<T> type, PreparedStatementSetter pss) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {
            pss.setValues(pstmt);
            return extractResults(pstmt, new ObjectMapper<>(type));
        } catch (SQLException e) {
            throw new DataAccessException(String.format("DB query sql 수행에 실패했습니다. sql = %s", sql), e);
        }
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... parameters) {
        return pstmt -> {
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }
        };
    }

    private <T> List<T> extractResults(PreparedStatement pstmt, ObjectMapper<T> objectMapper) throws SQLException{
        List<T> results = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                results.add(objectMapper.mapRow(rs));
            }
        }
        return results;
    }
}
