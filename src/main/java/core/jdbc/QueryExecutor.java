package core.jdbc;

import java.sql.PreparedStatement;

public interface QueryExecutor<T> {
    T executor(PreparedStatement ps);
}
