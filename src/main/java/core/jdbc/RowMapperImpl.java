package core.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperImpl implements RowMapper {

    @Override
    public <T> T getResultFromRow(Class<?> resultClazz, ResultSet resultSet) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.getResultObject(resultClazz, resultSet);
    }

    private <T> T getResultObject(Class<?> resultClazz, ResultSet resultSet) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        T result = (T) resultClazz.getConstructor().newInstance();
        Field[] fields = resultClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(result, this.getValueFromResultSet(resultSet, field.getName(), field.getType()));
        }

        return result;
    }

    private Object getValueFromResultSet(ResultSet resultSet, String filedName, Class<?> fieldType) throws SQLException {
        if (fieldType.equals(String.class)) {
            return resultSet.getString(filedName);
        }

        if (fieldType.equals(int.class)) {
            return resultSet.getInt(filedName);
        }

        // TODO 다른 종류 class 들에 대한 분기처리
        return null;
    }
}
