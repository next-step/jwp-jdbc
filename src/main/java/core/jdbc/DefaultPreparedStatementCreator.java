package core.jdbc;

import core.jdbc.exception.JdbcTemplateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultPreparedStatementCreator implements PreparedStatementCreator {

    private static final String ARGUMENT_COUNT_MISMATCH_MESSAGE = "인자의 수가 일치하지 않습니다. SQL 인자 수: %d, 입력 인자 수: %d";
    private final String sql;
    private final Object[] arguments;

    public DefaultPreparedStatementCreator(final String sql, final Object... arguments) {
        this.sql = sql;
        this.arguments = arguments;
    }

    @Override
    public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
        final PreparedStatement preparedStatement = getPreparedStatement(connection);
        validateArgumentCount(preparedStatement);
        for (int i = 0; i < arguments.length; i++) {
            preparedStatement.setObject((i + 1), arguments[i]);
        }
        return preparedStatement;
    }

    private void validateArgumentCount(final PreparedStatement preparedStatement) throws SQLException {
        final int parameterCount = preparedStatement.getParameterMetaData().getParameterCount();
        if (parameterCount != arguments.length) {
            throw new JdbcTemplateException(String.format(ARGUMENT_COUNT_MISMATCH_MESSAGE, parameterCount, arguments.length));
        }
    }

    private PreparedStatement getPreparedStatement(final Connection connection) throws SQLException {
        return connection.prepareStatement(this.sql);
    }

}
