package core.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectRowMapper<T> implements RowMapper<T> {
    private Class<T> entityType;

    public ObjectRowMapper(Class<T> entityType) {
        this.entityType = entityType;
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        try {
            Constructor<T> constructor = entityType.getConstructor();
            T entity = constructor.newInstance();
            Field[] fields = entityType.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(entity, rs.getString(field.getName()));
            }
            return entity;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataAccessException(e);
        }
    }
}
