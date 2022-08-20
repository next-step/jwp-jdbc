package core.jdbc;

import core.jdbc.exception.JdbcTemplateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcTemplate {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public int execute(final String sql, final Object... arguments) {
        initPreparedStatement(sql, arguments);
        try {

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        } finally {
            DataSourceUtils.release(connection, preparedStatement);
        }
    }

    public <T> Optional<T> queryForObject(final String sql, final RowMapperFunction<T> function, final Object... arguments) {
        initPreparedStatement(sql, arguments);

        try {
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(function.apply(resultSet));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        } finally {
            DataSourceUtils.release(connection, preparedStatement, resultSet);
        }
    }

    private void initPreparedStatement(final String sql, final Object[] arguments) {
        try {
            connection = ConnectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            setArguments(preparedStatement, arguments);
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    private void setArguments(final PreparedStatement preparedStatement, final Object[] arguments) throws SQLException {
        for (int i = 0; i < arguments.length; i++) {
            preparedStatement.setObject((i+1), arguments[i]);
        }
    }
}
