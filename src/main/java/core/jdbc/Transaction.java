package core.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction implements AutoCloseable {

    private final Connection connection;
    private boolean committed;

    public Transaction(final Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        this.connection = connection;
    }

    public void commit() throws SQLException {
        connection.commit();
        committed = true;
    }

    public boolean canRollback() {
        return !committed;
    }

    @Override
    public void close() throws SQLException {
        if (canRollback()) {
            connection.rollback();
        }
    }
}
