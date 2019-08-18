package next.dao;

/**
 * Created by youngjae.havi on 2019-08-18
 */
public class SqlException extends RuntimeException {
    public SqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlException(String s) {

    }
}
