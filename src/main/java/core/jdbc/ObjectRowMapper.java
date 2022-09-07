package core.jdbc;

import support.exception.ParameterClassNotFoundException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectRowMapper<T> implements RowMapper<T> {

    private final Class<T> resultClass;

    public ObjectRowMapper(Class<T> resultClass) {
        this.resultClass = resultClass;
    }

    @Override
    public T mapRow(ResultSet resultSet) {
        try {
            return getObjectFromRow(resultClass, resultSet);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private T getObjectFromRow(Class<T> resultClass, ResultSet resultSet) throws
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        T result = resultClass.getConstructor().newInstance();
        Field[] fields = resultClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(result, this.getValueFromRow(resultSet, field.getName(), field.getType()));
        }

        return result;
    }

    private Object getValueFromRow(ResultSet resultSet, String filedName, Class<?> fieldType) throws SQLException {
        if (fieldType.equals(String.class)) {
            return resultSet.getString(filedName);
        }

        if (fieldType.equals(int.class)) {
            return Integer.getInteger(String.valueOf(resultSet.getInt(filedName)));
        }

        throw new ParameterClassNotFoundException();
    }

}
