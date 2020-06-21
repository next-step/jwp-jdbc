package core.jdbc.callback;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementCallback<T> {
    T executeStatement(PreparedStatement ps) throws SQLException;
    String getSql();
}
