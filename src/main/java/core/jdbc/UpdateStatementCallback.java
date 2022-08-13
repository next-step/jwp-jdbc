package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.sql.Statement;

public class UpdateStatementCallback implements StatementCallback<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(UpdateStatementCallback.class);

    private final String sql;

    public UpdateStatementCallback(String sql) {
        this.sql = sql;
    }

    @Override
    public Integer doInStatement(Statement stmt) throws SQLException, DataAccessException {
        int rows = stmt.executeUpdate(sql);
        if (logger.isTraceEnabled()) {
            logger.trace("SQL update affected {} rows", rows);
        }
        return rows;
    }
}
