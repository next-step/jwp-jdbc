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
        execute(sql, preparedStatementSetter, PreparedStatement::executeUpdate);
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return queryForObject(sql, rowMapper, DefaultPreparedStatementSetter.from(parameters));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) {
        List<T> results = queryForList(sql, rowMapper, preparedStatementSetter);

        if (results.isEmpty()) {
            throw new NotExistsResultException("Query 결과가 존재하지 않습니다.");
        }

        if (results.size() != 1) {
            throw new NotUniqueResultException("Query 결과가 1개보다 많습니다.");
        }

        return results.get(0);
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return queryForList(sql, rowMapper, DefaultPreparedStatementSetter.from(parameters));
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) {
        return execute(sql, preparedStatementSetter, preparedStatement -> {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> result = new ArrayList<>();

                while (resultSet.next()) {
                    result.add(rowMapper.mapRow(resultSet));
                }

                return result;
            }
        });
    }

    private <T> T execute(String sql, PreparedStatementSetter preparedStatementSetter, PreparedStatementCallBack<T> preparedStatementCallBack) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatementSetter.setValues(preparedStatement);
            return preparedStatementCallBack.executeCallback(preparedStatement);
        } catch (SQLException e) {
            throw new QueryExecuteFailException(QUERY_EXECUTE_FAIL_EXCEPTION_MESSAGE_PREFIX + e.getCause().getMessage(), e);
        }
    }
}
