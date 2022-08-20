package core.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceUtils {

    private DataSourceUtils() {
        throw new AssertionError();
    }

    public static void releaseConnection(final Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void releaseStatement(final Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void releaseResultSet(final ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void release(final Connection connection, final Statement statement, final ResultSet resultSet) {
        releaseConnection(connection);
        releaseStatement(statement);
        releaseResultSet(resultSet);
    }

    public static void release(final Connection connection, final Statement statement) {
        release(connection, statement, null);
    }
}
