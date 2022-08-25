package core.jdbc.exception;

public class NotExistsResultException extends RuntimeException {
    public NotExistsResultException(String message) {
        super(message);
    }
}
