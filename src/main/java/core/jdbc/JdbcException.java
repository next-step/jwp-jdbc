package core.jdbc;

import java.sql.SQLException;

public class JdbcException extends RuntimeException {
    public JdbcException(SQLException ex) {
        super(ex);
    }
}
