package core.exception;

import java.sql.SQLException;

public class QueryExecutionException extends RuntimeException {

    public QueryExecutionException(SQLException exception) {
        super(exception);
    }
}
