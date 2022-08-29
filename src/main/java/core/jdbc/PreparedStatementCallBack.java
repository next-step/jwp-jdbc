package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementCallBack<T> {
    T executeCallback(PreparedStatement preparedStatement) throws SQLException;
}
