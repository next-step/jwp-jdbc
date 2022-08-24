package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementConsumer {

    void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value) throws SQLException;


}
