package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

class DefaultPreparedStatementSetter implements PreparedStatementSetter {
    private final List<Object> parameters;

    public DefaultPreparedStatementSetter(List<Object> parameters) {
        this.parameters = parameters;
    }

    public static DefaultPreparedStatementSetter from(Object... parameters) {
        return new DefaultPreparedStatementSetter(List.of(parameters));
    }

    @Override
    public void setValues(PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            preparedStatement.setObject(i + 1, parameters.get(i));
        }
    }
}
