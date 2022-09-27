package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultPreparedStatementSetter implements PreparedStatementSetter {

    private final List<Object> values = new ArrayList<>();
    public DefaultPreparedStatementSetter() {
    }

    public static PreparedStatementSetter empty() {
        return new DefaultPreparedStatementSetter();
    }

    public DefaultPreparedStatementSetter(List<Object> values) {
        this.values.addAll(values);
    }

    @Override
    public void setValues(PreparedStatement preparedStatement) throws SQLException {
        for (int index = 0; index < values.size(); index++) {
            setStatement(preparedStatement, index);
        }
    }

    private void setStatement(PreparedStatement preparedStatement, int index) throws SQLException {
        Object value = values.get(index);
        Class<?> clazz = value.getClass();

        if (isInteger(clazz)) {
            preparedStatement.setInt(index + 1, (int) value);
            return;
        }

        if (isDouble(clazz)) {
            preparedStatement.setDouble(index + 1, (double) value);
            return;
        }

        if (isLong(clazz)) {
            preparedStatement.setLong(index + 1, (long) value);
            return;
        }

        if (isString(clazz)) {
            preparedStatement.setString(index + 1, value.toString());
            return;
        }

        throw new IllegalArgumentException();
    }

    private boolean isString(Class<?> clazz) {
        return clazz == String.class;
    }

    private boolean isLong(Class<?> clazz) {
        return clazz == Long.class || clazz.equals(Long.TYPE);
    }

    private boolean isDouble(Class<?> clazz) {
        return clazz == Double.class || clazz.equals(Double.TYPE);
    }

    private boolean isInteger(Class<?> clazz) {
        return Integer.class == clazz || clazz.equals(Integer.TYPE);
    }
}
