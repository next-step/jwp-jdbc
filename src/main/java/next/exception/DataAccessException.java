package next.exception;

import java.sql.SQLException;

public class DataAccessException extends RuntimeException {
    public DataAccessException(SQLException e) {
        super(e);
    }
}
