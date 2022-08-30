package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.jdbc.ConnectionManager;

public class JdbcTemplate {

    public <T> T getOne(String query, RowMapper<T> function, Object... params) {
        var t = query(query, function, params);
        return t.get(0);
    }

    public <T> List<T> getAll(String query, RowMapper<T> function, Object... params) {
        return query(query, function, params);
    }

    private <T> List<T> query(String query, RowMapper<T> function, Object[] params) {
        try (var connection = getConnection();
             var preparedStatement = createPreparedStatement(connection, query, params);
             var resultSet = preparedStatement.executeQuery();
        ) {
            List<T> results = new ArrayList<>();

            while (resultSet.next()) {
                results.add(function.mapping(resultSet));
            }

            return results;
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    public void executeUpdate(String query, Object... params) {
        try (var connection = getConnection();
             var preparedStatement = createPreparedStatement(connection, query, params);
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement createPreparedStatement(Connection connection, String query, Object[] params) throws
        SQLException {
        var preparedStatement = connection.prepareStatement(query);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }

        return preparedStatement;
    }
}
