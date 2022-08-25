package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    public void update(PreparedStatementCreator preparedStatementCreator) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = preparedStatementCreator.createPreparedStatement(connection)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new QueryExecuteFailException("Query 수행중 에러 발생 - " + e.getCause().getMessage(), e);
        }
    }

    public <T> T queryForObject(PreparedStatementCreator preparedStatementCreator, RowMapper<T> rowMapper) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = preparedStatementCreator.createPreparedStatement(connection)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            T object;
            if (!resultSet.next()) {
                throw new RuntimeException("Query 결과가 존재하지 않습니다.");
            }

            object = rowMapper.mapRow(resultSet);

            if (resultSet.next()) {
                throw new RuntimeException("Query 결과가 1개보다 많습니다.");
            }

            return object;
        } catch (SQLException e) {
            throw new QueryExecuteFailException("Query 수행중 에러 발생 - " + e.getCause().getMessage(), e);
        }
    }
}
