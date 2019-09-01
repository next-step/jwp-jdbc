package next.jdbc.exception;

import java.sql.SQLException;

public class SQLRuntimeException extends RuntimeException {
    public SQLRuntimeException(final SQLException ex) {
        super(ex);
    }
}
