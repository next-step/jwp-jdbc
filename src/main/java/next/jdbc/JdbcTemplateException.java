package next.jdbc;

import java.sql.SQLException;

public class JdbcTemplateException extends RuntimeException {

    public JdbcTemplateException(final SQLException cause) {
        super(cause);
    }
}
