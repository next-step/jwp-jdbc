package next.jdbc;

import next.jdbc.exception.SQLRuntimeException;

import java.sql.SQLException;

public class PreparedStatementParameterSetterCreator {
    public static PreparedStatementParameterSetter create(final Object... queryParams) {
        return preparedStatement -> {
            try {
                int argIndex = 1;
                for (final Object value : queryParams) {
                    preparedStatement.setObject(argIndex, value);
                    argIndex++;
                }
            } catch (final SQLException ex) {
                throw new SQLRuntimeException(ex);
            }
        };
    }
}