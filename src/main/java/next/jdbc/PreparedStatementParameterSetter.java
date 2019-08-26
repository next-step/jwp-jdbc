package next.jdbc;

import java.sql.PreparedStatement;

@FunctionalInterface
public interface PreparedStatementParameterSetter {
    void setParameters(PreparedStatement preparedStatement);
}
