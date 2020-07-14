package next.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataAccessException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(DataAccessException.class);

    private String message;
    private Error errorType;
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

    public DataAccessException(Error errorType, Throwable cause) {
        super(errorType.toString(), cause);
        this.errorType = errorType;
        this.cause = cause;
    }

    public Error getErrorType() {
        return errorType;
    }

    public boolean isDuplicated() {
        return Error.DUPLICATED == errorType;
    }

    public enum Error {
        DUPLICATED
    }
}
