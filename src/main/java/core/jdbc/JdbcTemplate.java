package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private JdbcTemplate() {
    }

    public static JdbcTemplate getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final JdbcTemplate INSTANCE = new JdbcTemplate();
    }

    public void update(String sql, Object... arguments) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     new PreparedStatementCreatorImpl(sql, arguments).createPreparedStatement(connection)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T queryForObject(RowMapper<?> rowMapper, String sql, Object... arguments) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     new PreparedStatementCreatorImpl(sql, arguments).createPreparedStatement(connection);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            T result = null;

            if (resultSet.next()) {
                result = (T) rowMapper.mapRow(resultSet);
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> queryForList(RowMapper<?> rowMapper, String sql, Object... arguments) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     new PreparedStatementCreatorImpl(sql, arguments).createPreparedStatement(connection);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<T> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add((T) rowMapper.mapRow(resultSet));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
