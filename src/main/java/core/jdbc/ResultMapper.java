package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultMapper<T> {
    T mapResult(ResultSet rs) throws SQLException;
}
