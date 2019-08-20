package next.dao;

import java.sql.SQLException;

public class DataSQLException extends RuntimeException {
    public DataSQLException(Throwable e) {
        super(e);
    }
}
