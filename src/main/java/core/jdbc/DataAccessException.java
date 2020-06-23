package core.jdbc;

/**
 * @author KingCjy
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
