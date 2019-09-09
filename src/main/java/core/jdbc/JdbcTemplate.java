package core.jdbc;

import core.exception.QueryExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    public void executeQuery(String sql, Object... parameters) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            setParameters(preparedStatement, parameters);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new QueryExecutionException(e);
        }
    }

    public <T> T executeQuery(String sql, ResultMapper<T> mapper, Object... parameters) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, parameters);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return mapper.mapResult(rs);
            }

            return null;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new QueryExecutionException(e);
        }
    }

    public <T> List<T> executeQueryForList(String sql, ResultMapper<T> mapper, Object... parameters) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, parameters);

            ResultSet rs = preparedStatement.executeQuery();
            List<T> results = new ArrayList<>();
            while(rs.next()) {
                results.add(mapper.mapResult(rs));
            }

            return results;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new QueryExecutionException(e);
        }
    }

    private void setParameters(PreparedStatement preparedStatement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }
    }
}
