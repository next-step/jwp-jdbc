package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementSetter {

    /**
     * Set parameters on given PreparedStatement.
     * @param ps prepared statement
     * @throws SQLException ._.
     */
    void setValues(PreparedStatement ps) throws SQLException;
}
