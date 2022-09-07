package core.jdbc;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectMapper<T> implements RowMapper {

    private final LocalVariableTableParameterNameDiscoverer nameDiscoverer =
            new LocalVariableTableParameterNameDiscoverer();

    private final Class<T> objectType;

    public ObjectMapper(Class<T> objectType) {
        Assert.notNull(objectType, "objectType이 null이어선 안됩니다.");
        this.objectType = objectType;
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        try {
            Constructor<T> objectConstructor = objectType.getConstructor(
                    String.class, String.class, String.class, String.class);
            return newInstance(rs, getParameterNames(objectConstructor), objectConstructor);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("엔티티를 인스턴스화 하는데 실패했습니다.", e);
        }
    }

    private T newInstance(ResultSet rs, String[] parameterNames, Constructor<T> objectConstructor)
            throws InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {

        Object[] args = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            args[i] = rs.getObject(parameterNames[i]);
        }
        return objectConstructor.newInstance(args);
    }

    private String[] getParameterNames(Constructor<T> objectConstructor) {
        String[] parameterNames = nameDiscoverer.getParameterNames(objectConstructor);
        if (parameterNames == null) {
            throw new IllegalStateException("엔티티를 인스턴스화 하는데 실패했습니다.");
        }
        return parameterNames;
    }
}
