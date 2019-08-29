package next.jdbc;

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
                throw new RuntimeException(ex);
            }
        };
    }
}