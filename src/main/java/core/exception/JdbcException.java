package core.exception;

public class JdbcException extends RuntimeException {
    public JdbcException(ExceptionStatus status) {
        super(status.getMessage());
    }
}
