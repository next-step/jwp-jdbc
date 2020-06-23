package core.jdbc.exceptions;

public class UnableToAccessException extends RuntimeException {

    public UnableToAccessException(String message) {
        super(message);
    }

    public UnableToAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
