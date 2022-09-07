package core.jdbc;

import support.exception.DuplicatedEntityException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private JdbcTemplate() {

    }

    public static JdbcTemplate getInstance() {
        return jdbcTemplate;
    }

    public void update(String sql, Object... parameters) {
        this.update(sql, getPreparedStatementSetter(parameters));
    }

    public void update(String sql, PreparedStatementSetter preparedStatementSetter) {
        Connection connection = ConnectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatementSetter.setValues(preparedStatement);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return this.queryForObject(sql, rowMapper, getPreparedStatementSetter(parameters));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) {
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatementSetter.setValues(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            List<T> results = this.mapRows(resultSet, rowMapper);
            connection.close();

            return this.getSingleObject(results);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return this.query(sql, rowMapper, getPreparedStatementSetter(parameters));
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) {
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatementSetter.setValues(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            List<T> results = this.mapRows(resultSet, rowMapper);
            connection.close();

            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatementSetter getPreparedStatementSetter(Object... parameters) {
        return preparedStatement -> {
            int index = 0;
            for (Object parameter : parameters) {
                preparedStatement.setObject(index + 1, parameter);
                index++;
            }
        };
    }

    private <T> List<T> mapRows(ResultSet resultSet, RowMapper<T> rowMapper) throws SQLException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            results.add(rowMapper.mapRow(resultSet));
        }

        return results;
    }

    private <T> T getSingleObject(List<T> results) {
        if (results.isEmpty()) {
            return null;
        }

        if (results.size() != 1) {
            throw new DuplicatedEntityException();
        }

        return results.stream()
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
