package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface QueryExecutor<T> {
    T executor(PreparedStatement ps) throws SQLException;
}
