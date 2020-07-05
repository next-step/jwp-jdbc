package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
interface PrepareStatementCallback<T> {
    T doInPrepareStatement(PreparedStatement pstmt) throws SQLException;
}
