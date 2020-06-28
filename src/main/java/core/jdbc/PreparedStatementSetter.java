package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementSetter {
    void setValue(PreparedStatement preparedStatement) throws SQLException;
}
