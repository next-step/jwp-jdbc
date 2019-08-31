package next.jdbc.exception;

public class CannotConvertResultSetException extends RuntimeException {
    public CannotConvertResultSetException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
