package core.jdbc;

import core.jdbc.exception.DataAccessException;
import core.jdbc.exception.NonUniqueResultException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class JdbcTemplate {

    private static final JdbcTemplate INSTANCE = new JdbcTemplate();
    private static final Map<String, Object> EMPTY_PARAMETERS = Map.of();

    private JdbcTemplate() {
        if (INSTANCE != null) {
            throw new AssertionError(String.format("%s can not be instanced", getClass()));
        }
    }

    public static JdbcTemplate instance() {
        return INSTANCE;
    }

    public <T> Optional<T> queryForObject(String sql, Class<T> type) {
        return queryForObject(sql, type, EMPTY_PARAMETERS);
    }

    public <T> Optional<T> queryForObject(String sql, Class<T> type, Map<String, Object> parameters) {
        List<T> results = query(sql, type, parameters);
        if (results.size() > 1) {
            throw new NonUniqueResultException(String.format("query did not return a unique result: %d", results.size()));
        }
        return results.stream().findAny();
    }

    public <T> List<T> query(String sql, Class<T> type) {
        return query(sql, type, EMPTY_PARAMETERS);
    }

    public <T> List<T> query(String sql, Class<T> type, Map<String, Object> parameters) {
        QueryArgumentParser argumentParser = QueryArgumentParser.from(sql);
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(argumentParser.questionSymbolArgumentsSql())) {
            for (Map.Entry<Integer, Object> parameter : argumentParser.arguments(parameters).entrySet()) {
                pstmt.setObject(parameter.getKey(), parameter.getValue());
            }
            return extractResults(pstmt, new RowMapper<>(type));
        } catch (SQLException e) {
            throw new DataAccessException(String.format("query sql can not be executed(sql: %s, parameters: %s)", sql, parameters), e);
        }
    }

    public void update(String sql, Map<String, Object> parameters) {
        QueryArgumentParser argumentParser = QueryArgumentParser.from(sql);
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(argumentParser.questionSymbolArgumentsSql())) {
            for (Map.Entry<Integer, Object> parameter : argumentParser.arguments(parameters).entrySet()) {
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
