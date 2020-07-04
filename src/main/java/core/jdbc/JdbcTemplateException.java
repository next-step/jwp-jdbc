package core.jdbc;

public class JdbcTemplateException extends RuntimeException{


    public JdbcTemplateException(String message) {
        super(message);
    }

    public JdbcTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcTemplateException(Throwable cause) {
        super(cause);
    }
}
