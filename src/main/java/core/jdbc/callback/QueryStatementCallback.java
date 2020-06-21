package core.jdbc.callback;

import core.jdbc.resultset.ResultSetExtractor;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class QueryStatementCallback<T> extends AbstractStatementCallback<T> {
    private final ResultSetExtractor<T> resultSetExtractor;

    public QueryStatementCallback(String sql, ResultSetExtractor<T> resultSetExtractor) {
        this(sql, null, resultSetExtractor);
    }

    public QueryStatementCallback(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> resultSetExtractor) {
        super(sql, pss);

        if (Objects.isNull(resultSetExtractor)) {
            throw new IllegalArgumentException();
        }

        this.resultSetExtractor = resultSetExtractor;
    }

    @Override
    public T executeStatement(PreparedStatement stmt) throws SQLException {
        if (Objects.nonNull(pss)) {
            pss.setValues(stmt);
        }

        try(ResultSet rs = stmt.executeQuery()) {
            T result = resultSetExtractor.extractData(rs);
            return result;
        }
    }
}
