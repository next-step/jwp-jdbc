package core.jdbc.callback;

import core.jdbc.resultset.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class QueryStatementCallback<T> extends AbstractStatementCallback<T> {
    private final ResultSetExtractor<T> resultSetExtractor;

    public QueryStatementCallback(String sql, ResultSetExtractor<T> resultSetExtractor) {
        super(sql);

        if (Objects.isNull(resultSetExtractor)) {
            throw new IllegalArgumentException();
        }

        this.resultSetExtractor = resultSetExtractor;
    }

    @Override
    public T executeStatement(Statement stmt) throws SQLException {
        try(ResultSet rs = stmt.executeQuery(sql)) {
            T result = resultSetExtractor.extractData(rs);
            return result;
        }
    }
}
