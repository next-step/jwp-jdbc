package core.jdbc.exception;

public class JdbcTemplateException extends RuntimeException {

    public JdbcTemplateException(final String message) {
        super(message);
    }

    public JdbcTemplateException(final Throwable cause) {
        super(cause);
    }
}
