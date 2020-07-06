package core.jdbc.exception;

public class SqlExecuteException extends RuntimeException {

    public SqlExecuteException(String message, Throwable e) {
        super(message, e);
    }

    public SqlExecuteException(Throwable e) {
        super(e);
    }
}
