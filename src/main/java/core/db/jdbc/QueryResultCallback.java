package core.db.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface QueryResultCallback<T> {

    T apply(ResultSet rs) throws SQLException;

}