package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper<T> {

    T apply(ResultSet t) throws SQLException;

}
