package next.dao;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class ObjectRowMapper<T> implements RowMapper<T> {

    private Class<T> resultObject;

    public ObjectRowMapper(Class<T> resultObject) {
        this.resultObject = resultObject;
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        Object newInstance = null;
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            newInstance = resultObject.newInstance();
            Field[] declaredFields = resultObject.getDeclaredFields();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i + 1);

                Optional<Field> field = mappingColumnDataAtField(declaredFields, metaData.getColumnName(i + 1));
                setFileValue(newInstance, rs.getString(columnName), field);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) newInstance;
    }

    private void setFileValue(Object newInstance, String columnValue, Optional<Field> field) throws IllegalAccessException {
        if (field.isPresent()) {
            Field findField = field.get();
            findField.setAccessible(true);
            findField.set(newInstance, columnValue);
        }
    }

    private Optional<Field> mappingColumnDataAtField(Field[] declaredFields, String columnName) {
        return Arrays.stream(declaredFields)
                .filter(declaredField -> declaredField.getName().toLowerCase().equals(columnName.toLowerCase()))
                .findFirst();
    }
}
