package core.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author KingCjy
 */
public class DataSourceUtils {

    public static Connection getConnection(DataSource dataSource) {
        try {
            return doGetConnection(dataSource);
        } catch (SQLException e) {
            throw new JdbcTemplateException("Failed to Create JDBC Connection", e);
        }
    }

    public static Connection doGetConnection(DataSource dataSource) throws SQLException {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return dataSource.getConnection();
        }

        ConnectionHolder connectionHolder = TransactionSynchronizationManager.getResources(dataSource);

        if (connectionHolder != null) {
            return connectionHolder.getConnection();
        }

        Connection connection = dataSource.getConnection();

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            ConnectionHolder newConnectionHolder = new ConnectionHolder(dataSource, connection);
            TransactionSynchronizationManager.registerSynchronization(newConnectionHolder);
        }

        return connection;
    }

    public static void releaseConnection(Connection connection) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }

        if (connection == null) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new JdbcTemplateException("Failed to close JDBC Connection", e);
        }
    }
}
