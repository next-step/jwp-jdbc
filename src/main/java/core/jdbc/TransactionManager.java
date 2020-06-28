package core.jdbc;

import core.jdbc.exceptions.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class TransactionManager {

    private static final Logger log = LoggerFactory.getLogger(TransactionManager.class);

    private static final ThreadLocal<Connection> connection = new ThreadLocal<>();

    public static Connection getConnection() {
        beginTransaction();
        return TransactionManager.connection.get();
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
            TransactionManager.connection.remove();
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
            TransactionManager.connection.remove();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("._. rollback error");
        }
    }

    public static <T> T processTransaction(Supplier<T> supplier) {
        try {
            beginTransaction();
            final T result = supplier.get();
            commit();
            return result;
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            rollback();
            throw new DataAccessException("Critical error occurred during transaction");
        }
    }
}
