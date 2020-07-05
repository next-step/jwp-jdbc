package core.jdbc.exception;

public class JdbcRuntimeException extends RuntimeException {

    public JdbcRuntimeException(String message) {
        super(message);
    }

    public JdbcRuntimeException(Throwable cause) {
        super(cause);
    }

    public JdbcRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
