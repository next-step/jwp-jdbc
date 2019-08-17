package core.db.jdbc;

/**
 * @author : yusik
 * @date : 2019-08-17
 */
public class JdbcException extends RuntimeException {

    public JdbcException() {
        super();
    }

    public JdbcException(String message, Throwable cause) {
        super(message, cause);
    }
}
