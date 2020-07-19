package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface QueryCallback<T> {
    T mapping(ResultSet rs) throws SQLException;
}
