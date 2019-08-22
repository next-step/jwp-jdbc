package core.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.jdbc.core.RowMapper;
import support.exception.ConstructorNotFoundException;
import support.exception.CouldNotCreateInstanceException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class AllArgumentRowMapper<T> implements RowMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(AllArgumentRowMapper.class);

    private Class<T> type;

    public AllArgumentRowMapper(Class<T> type) {
        this.type = type;
    }

    @Override
    public T mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Class<?>[] types = getFieldTypes();
        Constructor<?> constructor = getConstructor(types);
        Object[] parameters = getParameters(resultSet, constructor);

        return getInstance(constructor, parameters);
    }

    @SuppressWarnings("unchecked")
    private T getInstance(Constructor<?> constructor, Object[] parameters) {
        try {
            return (T) constructor.newInstance(parameters);
        } catch (ReflectiveOperationException e) {
            logger.error(e.getMessage());
            throw new CouldNotCreateInstanceException(e.getMessage());
        }
    }

    private Object[] getParameters(ResultSet resultSet, Constructor<?> constructor) throws SQLException {
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(constructor);
        Object[] parameters = new Object[parameterNames.length];

        for (int i = 0; i < parameterNames.length; i++) {
            parameters[i] = resultSet.getObject(parameterNames[i]);
        }
        return parameters;
    }

    private Constructor<?> getConstructor(Class<?>[] types) throws ConstructorNotFoundException {
        return Arrays.stream(type.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterCount() == types.length)
                .findFirst()
                .orElseThrow(() -> new ConstructorNotFoundException("AllArgumentConstructor 를 찾을 수 없습니다."));
    }

    private Class<?>[] getFieldTypes() {
        return Arrays.stream(type.getDeclaredFields())
                .map(Field::getType)
                .toArray(Class<?>[]::new);
    }
}
