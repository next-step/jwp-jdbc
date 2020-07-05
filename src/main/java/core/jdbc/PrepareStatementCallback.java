package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

interface PrepareStatementCallback<T> {
    T doInPrepareStatement(PreparedStatement pstmt) throws SQLException;
}
