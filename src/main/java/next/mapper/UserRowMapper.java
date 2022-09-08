package next.mapper;

import core.jdbc.RowMapper;
import next.model.User;
import support.exception.ParameterClassNotFoundException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet) {
        try {
            return getObjectFromRow(resultSet);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private User getObjectFromRow(ResultSet resultSet) throws
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        User result = User.class.getConstructor().newInstance();
        Field[] fields = User.class.getDeclaredFields();
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
