package core.jdbc;

import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryStatementCallback<T> implements StatementCallback<T> {

    private final String sql;
    private final ResultSetExtractor<T> rse;

    public QueryStatementCallback(String sql, ResultSetExtractor<T> rse) {
        this.sql = sql;
        this.rse = rse;
    }

    @Override
    public T doInStatement(Statement stmt) throws SQLException, DataAccessException {
        try (ResultSet rs = stmt.executeQuery(sql)) {
            return rse.extractData(rs);
        }
    }
}
