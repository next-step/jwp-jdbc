package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementParameterSetter {
    void set(PreparedStatement pstmt) throws SQLException;
}
