package next.exception;

import core.jdbc.error.JdbcErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataAccessException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(DataAccessException.class);

    private String message;
    private JdbcErrorType errorType;
    private Throwable cause;

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }

    public DataAccessException(String message) {
        super(message);
        this.message = message;
    }

    public DataAccessException(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    public DataAccessException(JdbcErrorType errorType, Throwable cause) {
        super(errorType.toString(), cause);
        this.errorType = errorType;
        this.cause = cause;
    }

    public JdbcErrorType getErrorType() {
        return errorType;
    }

    public boolean isDuplicated() {
        return JdbcErrorType.DUPLICATE_KEY == errorType;
    }
}
