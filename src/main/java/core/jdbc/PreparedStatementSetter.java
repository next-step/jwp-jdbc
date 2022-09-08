package core.jdbc;

import java.sql.SQLException;

public interface PreparedStatementSetter<T> {

    void setValues(T preparedStatement) throws SQLException;

}
