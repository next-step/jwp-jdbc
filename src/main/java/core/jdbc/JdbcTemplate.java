package core.jdbc;

import core.jdbc.exception.NotExistsResultException;
import core.jdbc.exception.NotUniqueResultException;
import core.jdbc.exception.QueryExecuteFailException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final String QUERY_EXECUTE_FAIL_EXCEPTION_MESSAGE_PREFIX = "Query 수행중 에러 발생 - ";

    public void update(String sql, Object... parameters) {
        update(sql, DefaultPreparedStatementSetter.from(parameters));
    }

    public void update(String sql, PreparedStatementSetter preparedStatementSetter) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = createPreparedStatement(connection, sql, preparedStatementSetter)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new QueryExecuteFailException(QUERY_EXECUTE_FAIL_EXCEPTION_MESSAGE_PREFIX + e.getCause().getMessage(), e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = createPreparedStatement(connection, sql, parameters);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (!resultSet.next()) {
                throw new NotExistsResultException("Query 결과가 존재하지 않습니다.");
            }

            T object = rowMapper.mapRow(resultSet);

            if (resultSet.next()) {
                throw new NotUniqueResultException("Query 결과가 1개보다 많습니다.");
            }

            return object;
        } catch (SQLException e) {
            throw new QueryExecuteFailException(QUERY_EXECUTE_FAIL_EXCEPTION_MESSAGE_PREFIX + e.getCause().getMessage(), e);
        }
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... parameters) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = createPreparedStatement(connection, sql, parameters);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<T> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet));
            }

            return result;
        } catch (SQLException e) {
            throw new QueryExecuteFailException(QUERY_EXECUTE_FAIL_EXCEPTION_MESSAGE_PREFIX + e.getCause().getMessage(), e);
        }
    }

    private PreparedStatement createPreparedStatement(Connection connection, String sql, PreparedStatementSetter preparedStatementSetter) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatementSetter.setValues(preparedStatement);
        return preparedStatement;
    }

    private PreparedStatement createPreparedStatement(Connection connection, String sql, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }
        return preparedStatement;
    }
}
