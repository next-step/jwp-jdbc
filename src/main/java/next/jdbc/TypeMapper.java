package next.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TypeMapper<T> implements ResultMapper<T> {

    private final Class<T> clazz;

    public TypeMapper(final Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T mapping(final ResultSet resultSet) throws SQLException {
        final T instance = newInstance();

        for (final Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            final String name = field.getName();
            final Object value = resultSet.getObject(name);

            bindField(instance, field, value);
        }

        return instance;
    }

    private T newInstance() {
        try {
            return getDefaultConstructor().newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException(e);
        }

    }

    private Constructor<T> getDefaultConstructor() {
        try {
            return clazz.getDeclaredConstructor();
        } catch (final NoSuchMethodException e) {
            throw new DefaultConstructorNotFoundException(e);
        }
    }

    private void bindField(final T instance,
                           final Field field,
                           final Object value) {
        try {
            field.set(instance, value);
        } catch (final IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }
}
