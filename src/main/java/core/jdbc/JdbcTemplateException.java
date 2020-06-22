package core.jdbc;

/**
 * @author KingCjy
 */
public class JdbcTemplateException extends RuntimeException {

    public JdbcTemplateException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
