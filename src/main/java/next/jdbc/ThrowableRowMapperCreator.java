package next.jdbc;

import next.jdbc.exception.CannotConvertResultSetException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class ThrowableRowMapperCreator {
    public static <T> ThrowableRowMapper<T> create(final Class<T> mapToClass) {
        return resultSet -> {
            try {
                final T resultInstance = newInstance(mapToClass);
                setFields(mapToClass, resultSet, resultInstance);
                return resultInstance;
            } catch (final ReflectiveOperationException roex) {
                throw new CannotConvertResultSetException(roex.getMessage(), roex);
            }
        };
    }

    private static <T> void setFields(final Class<T> mapToClass, final ResultSet resultSet, final T resultInstance) throws SQLException, IllegalAccessException {
        final Field[] declaredFields = mapToClass.getDeclaredFields();

        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            final String columnLabel = metaData.getColumnLabel(i);
            final Object value = resultSet.getObject(i);
            setField(columnLabel, value, resultInstance, declaredFields);
        }
    }

    private static <T> void setField(final String columnLabel, final Object value, final T resultInstance, final Field[] declaredFields) throws IllegalAccessException {
        final Optional<Field> optionalField = Arrays.stream(declaredFields)
                .filter(f -> f.getName().toUpperCase().equals(columnLabel))
                .findFirst();
        if (!optionalField.isPresent()) {
            return;
        }

        final Field field = optionalField.get();
        field.setAccessible(true);
        field.set(resultInstance, value);
        field.setAccessible(false);
    }

    private static <T> T newInstance(final Class<T> mapToClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        final Constructor<T> constructor = mapToClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        final T resultInstance = constructor.newInstance();
        constructor.setAccessible(false);
        return resultInstance;
    }
}
