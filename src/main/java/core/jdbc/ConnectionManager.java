package core.jdbc;

import java.sql.Connection;

public interface ConnectionManager {
    Connection getConnection();
}
