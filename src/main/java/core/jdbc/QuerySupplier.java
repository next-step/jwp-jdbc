package core.jdbc;

import java.sql.SQLException;

@FunctionalInterface
public interface QuerySupplier<T> {

    T get() throws SQLException;
}
