package core.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author KingCjy
 */
public class TransactionSynchronizationManager {

    private static final ThreadLocal<ConnectionHolder> resources = new ThreadLocal<>();
    private static final ThreadLocal<Connection> connections = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> actives = new ThreadLocal<>();

    public static void initSynchronization() {
        if (isSynchronizationActive()) {
            throw new IllegalStateException("Cannot activate transaction synchronization - already actives");
        }

        actives.set(true);
    }

    public static boolean isSynchronizationActive() {
        return (actives.get() != null && actives.get() == true);
    }

    public static ConnectionHolder getResources() {
        return resources.get();
    }

    public static void registerSynchronization(ConnectionHolder connectionHolder) {
        resources.set(connectionHolder);
        connections.set(connectionHolder.getConnection());
    }

    public static void unbindResource() {
        resources.set(null);
    }

    public static void clearSynchronization() {
        resources.set(null);
        connections.set(null);
        actives.set(false);
    }
}
