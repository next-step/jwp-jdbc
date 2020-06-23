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
            throw new DataAccessException("Failed to Create JDBC Connection", e);
        }
    }

    public static Connection doGetConnection(DataSource dataSource) throws SQLException {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return dataSource.getConnection();
        }

        ConnectionHolder connectionHolder = TransactionSynchronizationManager.getResources();

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
        try {
            doReleaseConnection(connection);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to close JDBC Connection", e);
        }
    }

    public static void doReleaseConnection(Connection connection) throws SQLException {
        if(connection == null) {
            return;
        }

        ConnectionHolder connectionHolder = TransactionSynchronizationManager.getResources();

        if(connectionHolder != null && connection == connectionHolder.getConnection()) {
            connectionHolder.release();
            TransactionSynchronizationManager.unbindResource();
            return;
        }

        doCloseConnection(connection);
    }

    private static void doCloseConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
