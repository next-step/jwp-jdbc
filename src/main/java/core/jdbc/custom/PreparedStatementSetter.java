package core.jdbc.custom;

import java.sql.PreparedStatement;

@FunctionalInterface
public interface PreparedStatementSetter {
    void setValues(PreparedStatement preparedStatement);
}
