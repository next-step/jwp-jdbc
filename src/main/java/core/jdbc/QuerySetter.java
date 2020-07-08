package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface QuerySetter {
    void queryValues(PreparedStatement pstmt) throws SQLException;
}
