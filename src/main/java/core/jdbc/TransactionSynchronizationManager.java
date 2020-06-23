package core.jdbc;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author KingCjy
 */
public class TransactionSynchronizationManager {

    private static final ThreadLocal<Map<DataSource, ConnectionHolder>> resources = new ThreadLocal<>();

    public static void initSynchronization() {
        if (isSynchronizationActive()) {
            throw new IllegalStateException("Cannot activate transaction synchronization - already active");
        }

        resources.set(new LinkedHashMap<>());
    }

    public static boolean isSynchronizationActive() {
        return (resources.get() != null);
    }

    public static ConnectionHolder getResources(DataSource dataSource) {
        if (resources.get() == null) {
            throw new IllegalStateException("Cannot find Resources");
        }

        return resources.get().get(dataSource);
    }

    public static void registerSynchronization(ConnectionHolder connectionHolder) {
        resources.get().put(connectionHolder.getDataSource(), connectionHolder);
    }

    public static void clearSynchronization() {
        resources.set(null);
    }
}
