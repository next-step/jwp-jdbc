package core.db.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : yusik
 * @date : 2019-08-19
 */
public class ResultTypeRowMapper<T> implements RowMapper<T> {

    private static final Logger logger = LoggerFactory.getLogger(ResultTypeRowMapper.class);

    private final Class<T> resultType;

    public ResultTypeRowMapper(Class<T> resultType) {
        this.resultType = resultType;
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        T instance = null;
        try {
            instance = newInstance();
            setInstanceFields(instance, rs);
        } catch (ReflectiveOperationException e) {
            logger.error("ResultTypeRowMapper<{}> : {}", resultType, e.getMessage(), e);
        }

        return instance;
    }

    private T newInstance() throws ReflectiveOperationException {
        Constructor<T> constructors = resultType.getDeclaredConstructor();
        ReflectionUtils.makeAccessible(constructors);
        return constructors.newInstance();
    }

    private void setInstanceFields(T instance, ResultSet rs) throws SQLException, ReflectiveOperationException {
        Field[] fields = resultType.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            Object value = rs.getObject(fieldName);

            setField(instance, fields[i], value);
        }
    }

    private void setField(T instance, Field field, Object value) throws IllegalAccessException {
        if (value != null) {
            ReflectionUtils.makeAccessible(field);
            field.set(instance, value);
        }
    }
}
