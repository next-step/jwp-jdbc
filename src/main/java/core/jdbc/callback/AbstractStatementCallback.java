package core.jdbc.callback;

import core.jdbc.argumentsetter.PreparedStatementSetter;
import core.util.StringUtils;

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
}
