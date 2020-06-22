package core.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author KingCjy
 */
public class ConnectionHolder {

    private DataSource dataSource;
    private Connection connection;

    public ConnectionHolder(DataSource dataSource, Connection connection) {
        this.dataSource = dataSource;
        this.connection = connection;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() {
        return connection;
    }
}
