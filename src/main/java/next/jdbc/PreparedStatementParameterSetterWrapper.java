package next.jdbc;

import java.sql.SQLException;

public class PreparedStatementParameterSetterWrapper {
    public static PreparedStatementParameterSetter setParameters(
            final ThrowablePreparedStatementParameterSetter parameterSetter) {
        return preparedStatement -> {
            try {
                parameterSetter.setParameters(preparedStatement);
            } catch (final SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
