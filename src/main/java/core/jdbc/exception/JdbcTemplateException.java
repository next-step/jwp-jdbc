package core.jdbc.exception;

public class JdbcTemplateException extends RuntimeException {

    public JdbcTemplateException(String message, Throwable e) {
        super(message, e);
    }

    public JdbcTemplateException(Throwable e) {
        super(e);
    }

    public JdbcTemplateException(String message) {
        super(message);
    }
}
