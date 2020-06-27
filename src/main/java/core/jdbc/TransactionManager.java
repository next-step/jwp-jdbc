package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

    private static final Logger log = LoggerFactory.getLogger(TransactionManager.class);

    private static final ThreadLocal<Connection> connection = new ThreadLocal<>();

    public static Connection getConnection() {
        Connection conn = TransactionManager.connection.get();
        if (conn == null) {
            beginTransaction();
            conn = TransactionManager.connection.get();
        }
        return conn;
    }

    public static void beginTransaction() {
        if (TransactionManager.connection.get() != null) {
            return;
        }

        try {
            final Connection conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);
            TransactionManager.connection.set(conn);
            log.debug("transaction has been started!");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error occurred on beginTransaction");
        }
    }

    public static void commit() {
        try {
            final Connection conn = TransactionManager.connection.get();
            conn.commit();
            log.debug("commit - success");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error occurred on commit");
        }
    }

    public static void rollback() {
        try {
            final Connection conn = TransactionManager.connection.get();
            conn.rollback();
            log.debug("rollback - success");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("._. rollback error");
        }
    }
}
