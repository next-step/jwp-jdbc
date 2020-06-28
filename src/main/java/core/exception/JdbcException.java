package core.exception;

public class JdbcException extends RuntimeException {
    public JdbcException(ExceptionStatus status) {
        super(status.getMessage());
    }

    public JdbcException(ExceptionStatus status, Throwable e) {
        super(status.getMessage(), e);
    }
}
