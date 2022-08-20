package core.jdbc;

import core.jdbc.exception.MappingException;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

final class RowMapper<T> {

    private static final LocalVariableTableParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    private final Class<T> objectType;

    RowMapper(Class<T> objectType) {
        Assert.notNull(objectType, "'objectType' must not be null");
        this.objectType = objectType;
    }

    T map(ResultSet resultSet) throws SQLException {
        try {
            Constructor<T> objectConstructor = constructor();
            return newInstance(resultSet, parameterNames(objectConstructor), objectConstructor);
        } catch (ReflectiveOperationException e) {
            throw new MappingException(String.format("could not instantiate entity %s", objectType), e);
        }
    }

    private String[] parameterNames(Constructor<T> objectConstructor) {
        String[] parameterNames = NAME_DISCOVERER.getParameterNames(objectConstructor);
        if (parameterNames == null) {
            throw new MappingException(String.format("%s constructor parameter names can not be null", objectType));
        }
        return parameterNames;
    }

    private T newInstance(ResultSet resultSet, String[] parameterNames, Constructor<T> constructor)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, SQLException {
        Object[] args = new Object[parameterNames.length];
        int index = 0;
        for (String parameterName : parameterNames) {
            args[index++] = resultSet.getObject(parameterName);
        }
        return constructor.newInstance(args);
    }

    @SuppressWarnings("unchecked")
    private Constructor<T> constructor() {
        return (Constructor<T>) Stream.of(objectType.getConstructors())
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException(
                        String.format("objectType(%s) must have any constructor", objectType)));
    }
}
