package core.jdbc;

import core.jdbc.exception.JdbcTemplateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {

    public int execute(final String insertSql, final Object... arguments) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionManager.getConnection();
            preparedStatement = connection.prepareStatement(insertSql);

            setArguments(preparedStatement, arguments);

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        } finally {
            DataSourceUtils.release(connection, preparedStatement);
        }
    }

    private static void setArguments(final PreparedStatement preparedStatement, final Object[] arguments) throws SQLException {
        for (int i = 0; i < arguments.length; i++) {
            preparedStatement.setObject((i+1), arguments[i]);
        }
    }
}
