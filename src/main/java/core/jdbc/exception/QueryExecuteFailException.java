package core.jdbc.exception;

public class QueryExecuteFailException extends RuntimeException {
    public QueryExecuteFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
