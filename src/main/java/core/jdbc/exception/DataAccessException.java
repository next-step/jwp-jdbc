package core.jdbc.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException() {
    }

    public DataAccessException(String message) {
        super(message);
    }
}
