package core.jdbc;

import core.jdbc.exception.JdbcTemplateException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public enum PreparedStatementTypeSetter {

    STRING(String.class, (preparedStatement, parameterIndex, value) -> preparedStatement.setString(parameterIndex, (String) value)),
    INTEGER(Integer.class, (preparedStatement, parameterIndex, value) -> preparedStatement.setInt(parameterIndex, (Integer) value));

    private static final String UNSUPPORTED_TYPE = "지원하지 않는 타입의 값입니다. 타입: %s, 값: %s";


    private final Class<?> parameterType;
    private final PreparedStatementConsumer consumer;

    PreparedStatementTypeSetter(final Class<?> parameterType, final PreparedStatementConsumer consumer) {
        this.parameterType = parameterType;
        this.consumer = consumer;
    }

    public static void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value) throws SQLException {
        Arrays.stream(values())
            .filter(it -> value.getClass().isAssignableFrom(it.parameterType))
            .findAny()
            .orElseThrow(() -> new JdbcTemplateException(String.format(UNSUPPORTED_TYPE, value.getClass(), value)))
            .consumer
            .setValue(preparedStatement, parameterIndex, value);
    }
}
