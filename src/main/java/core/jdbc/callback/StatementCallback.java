package core.jdbc.callback;

import java.sql.SQLException;
import java.sql.Statement;

public interface StatementCallback<T> {
    T executeStatement(Statement stmt) throws SQLException;
    String getSql();
}
