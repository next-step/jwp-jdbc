package core.jdbc;

import core.jdbc.exception.JdbcTemplateException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultPreparedStatementSetter implements PreparedStatementSetter {

    private static final String ARGUMENT_COUNT_MISMATCH_MESSAGE = "인자의 수가 일치하지 않습니다. SQL 인자 수: %d, 입력 인자 수: %d";

    private final Object[] arguments;

    public DefaultPreparedStatementSetter(final Object... arguments) {
        this.arguments = arguments;
    }

    @Override
    public void setValues(final PreparedStatement preparedStatement) throws SQLException {
        validateState(preparedStatement);
        validateArgumentCount(preparedStatement);
        for (int i = 0; i < arguments.length; i++) {
            PreparedStatementTypeSetter.setValue(preparedStatement, (i + 1), arguments[i]);
        }
    }

    private void validateState(final PreparedStatement preparedStatement) throws SQLException {
        if (preparedStatement.isClosed()) {
            throw new JdbcTemplateException("prepared statement is closed");
        }
    }

    private void validateArgumentCount(final PreparedStatement preparedStatement) throws SQLException {
        final int parameterCount = preparedStatement.getParameterMetaData().getParameterCount();
        if (parameterCount != arguments.length) {
            throw new JdbcTemplateException(String.format(ARGUMENT_COUNT_MISMATCH_MESSAGE, parameterCount, arguments.length));
        }
    }
}
