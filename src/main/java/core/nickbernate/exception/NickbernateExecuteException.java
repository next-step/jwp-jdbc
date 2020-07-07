package core.nickbernate.exception;

public class NickbernateExecuteException extends IllegalStateException {

    public NickbernateExecuteException(String message, Throwable e) {
        super(message, e);
    }
}
