package core.jdbc.custom;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultRowMapper<T> implements RowMapper<T> {

    Class clazz;
    public DefaultRowMapper(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public T mapRowForObject(final ResultSet resultSet) throws Exception {
        if (resultSet.next()) {
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int columnCount = metaData.getColumnCount();

            final Map<String, String> columns = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                final String columnName = metaData.getColumnName(i);
                final String columnValue = resultSet.getString(i);

                columns.put(columnName, columnValue);
            }
            final Class[] types = Arrays.stream(clazz.getDeclaredFields())
                    .map(Field::getType)
                    .toArray(Class[]::new);
            final Object[] objects = Arrays.stream(clazz.getDeclaredFields())
                    .map(field -> columns.get(field.getName().toUpperCase()))
                    .toArray();

            final Constructor constructor = clazz.getDeclaredConstructor(types);
            return (T) constructor.newInstance(objects);
        }
        return null;
    }
}
