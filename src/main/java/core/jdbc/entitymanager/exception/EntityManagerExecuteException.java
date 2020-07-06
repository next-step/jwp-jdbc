package core.jdbc.entitymanager.exception;

public class EntityManagerExecuteException extends IllegalStateException {

    public EntityManagerExecuteException(String message, Throwable e) {
        super(message, e);
    }
}
