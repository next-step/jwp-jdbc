package core.jdbc.exception;

public class NotUniqueResultException extends RuntimeException {
    public NotUniqueResultException(String message) {
        super(message);
    }
}
