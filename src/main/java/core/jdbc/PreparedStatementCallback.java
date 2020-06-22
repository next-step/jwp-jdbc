package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author KingCjy
 */
public interface PreparedStatementCallback<T> {
    T doInstatement(PreparedStatement preparedStatement) throws SQLException;
}
