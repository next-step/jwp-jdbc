package core.jdbc;

import core.jdbc.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final String ERROR_MESSAGE = "There are more than two results.";
    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public static JdbcTemplate getInstance() {
        return jdbcTemplate;
    }

    public void update(String sql, Object... parameters) {
        update(sql, createPreparedStatementSettersByObjects(parameters));
    }

    public void update(String sql, PreparedStatementSetter pstmts) {
        Connection connection = ConnectionManager.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmts.setValues(pstmt);
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return this.queryForObject(sql, rowMapper, createPreparedStatementSettersByObjects(parameters));
    }


    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmts) {
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql);) {
            pstmts.setValues(pstmt);
            resultSet = pstmt.executeQuery();

            List<T> results = extractResults(resultSet, rowMapper);
            connection.close();

            return getSingleObject(results);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private <T> T getSingleObject(List<T> results) {
        if (results.isEmpty()) {
            return null;
        }

        if (results.size() != 1) {
            throw new DataAccessException(ERROR_MESSAGE);
        }

        return results.stream()
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return this.query(sql, rowMapper, createPreparedStatementSettersByObjects(parameters));
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmts) {
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmts.setValues(pstmt);
            resultSet = pstmt.executeQuery();
            List<T> results = extractResults(resultSet, rowMapper);
            connection.close();

            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> extractResults(ResultSet resultSet, RowMapper<T> rowMapper) throws SQLException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            results.add(rowMapper.mapRow(resultSet));
        }

        return results;
    }

    private PreparedStatementSetter createPreparedStatementSettersByObjects(Object... parameters) {
        return pstmt -> {
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }
        };
    }
}
