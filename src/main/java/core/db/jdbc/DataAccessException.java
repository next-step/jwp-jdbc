package core.db.jdbc;

/**
 * @author : yusik
 * @date : 2019-08-17
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException() {
        super();
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
