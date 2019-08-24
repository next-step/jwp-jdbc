package core.jdbc;

import java.sql.SQLException;

public class DataSQLException extends RuntimeException {
    public DataSQLException(Throwable e) {
        super(e);
    }
}
