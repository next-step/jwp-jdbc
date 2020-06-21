package core.jdbc.callback;

import core.jdbc.argumentsetter.PreparedStatementSetter;
import core.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public abstract class AbstractStatementCallback<T> implements StatementCallback<T> {
    protected final String sql;
    protected final PreparedStatementSetter pss;

    protected AbstractStatementCallback(String sql) {
        this(sql, null);
    }

    public AbstractStatementCallback(String sql, PreparedStatementSetter pss) {
        if (StringUtils.isEmpty(sql)) {
            throw new IllegalArgumentException();
        }

        this.sql = sql;
        this.pss = pss;
    }

    @Override
    public String getSql() {
        return sql;
    }

    public void setPreparedStatementValues(PreparedStatement ps) throws SQLException {
        if (Objects.nonNull(pss)) {
            pss.setValues(ps);
        }
    }
}
