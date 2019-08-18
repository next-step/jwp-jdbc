package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface QueryAction<T> {
    T action(PreparedStatement ps) throws SQLException;
}
