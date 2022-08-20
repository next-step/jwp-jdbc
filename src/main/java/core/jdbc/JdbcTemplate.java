package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class JdbcTemplate {

    private static final JdbcTemplate INSTANCE = new JdbcTemplate();
    private static final Map<Integer, Object> EMPTY_PARAMETERS = Map.of();

    private JdbcTemplate() {
        if (INSTANCE != null) {
            throw new AssertionError(String.format("%s can not be instanced", getClass()));
        }
    }

    public static JdbcTemplate instance() {
        return INSTANCE;
    }

    public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper) {
        return queryForObject(sql, rowMapper, EMPTY_PARAMETERS);
    }

    public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, Map<Integer, Object> parameters) {
        return query(sql, rowMapper, parameters).stream().findAny();
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return query(sql, rowMapper, EMPTY_PARAMETERS);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Map<Integer, Object> parameters) {
        try (PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql)) {
            for (Map.Entry<Integer, Object> parameter : parameters.entrySet()) {
                pstmt.setObject(parameter.getKey(), parameter.getValue());
            }
            return extractResults(pstmt, rowMapper);
        } catch (SQLException e) {
            throw new DataAccessException(String.format("query sql can not be executed(sql: %s, parameters: %s)", sql, parameters), e);
        }
    }

    public void update(String sql, Map<Integer, Object> parameters) {
        try (PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql)) {
            for (Map.Entry<Integer, Object> parameter : parameters.entrySet()) {
                pstmt.setObject(parameter.getKey(), parameter.getValue());
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(String.format("update sql can not be executed(sql: %s, parameters: %s)", sql, parameters), e);
        }
    }

    private <T> List<T> extractResults(PreparedStatement pstmt, RowMapper<T> rowMapper) throws SQLException {
        List<T> results = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                results.add(rowMapper.map(rs));
            }
        }
        return results;
    }
}
