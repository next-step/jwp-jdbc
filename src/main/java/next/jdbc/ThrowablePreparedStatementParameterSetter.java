package next.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ThrowablePreparedStatementParameterSetter {
    void setParameters(PreparedStatement preparedStatement) throws SQLException;
}