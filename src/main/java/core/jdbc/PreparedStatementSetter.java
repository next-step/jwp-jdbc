package core.jdbc;

import java.sql.PreparedStatement;

@FunctionalInterface
public interface PreparedStatementSetter {

    void setValues(PreparedStatement pstmt);
}
