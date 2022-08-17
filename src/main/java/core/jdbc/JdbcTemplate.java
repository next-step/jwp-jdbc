package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
