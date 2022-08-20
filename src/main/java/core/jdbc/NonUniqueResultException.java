package core.jdbc;

public class NonUniqueResultException extends RuntimeException {

    public NonUniqueResultException(String message) {
        super(message);
    }
}

