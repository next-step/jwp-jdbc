package core.jdbc.callback;

import core.util.StringUtils;

public abstract class AbstractStatementCallback<T> implements StatementCallback<T> {
    protected final String sql;

    protected AbstractStatementCallback(String sql) {
        if (StringUtils.isEmpty(sql)) {
            throw new IllegalArgumentException();
        }

        this.sql = sql;
    }

    @Override
    public String getSql() {
        return sql;
    }
}
