package core.jdbc;

import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementSetter<T> {
    void setValues(T t) throws SQLException;
}
