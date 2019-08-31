package next.dao.mapper.row;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.exception.DataAccessException;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

public class BasicRowMapper<T> implements RowMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(BasicRowMapper.class);

    private Class<T> clazz;
    private Map<String, Field> fields;

    public BasicRowMapper(Class<T> clazz) {
        this.clazz = clazz;
        initFields(clazz);
    }

    private void initFields(Class<T> clazz) {
        fields = new HashMap<>();
        Stream.of(clazz.getDeclaredFields())
                .forEach(field -> fields.put(field.getName(), field));
    }

    @Override
    public T mapObject(ResultSet rs) {
        T result = null;
        try {
            result = clazz.newInstance();
            Set<String> columnNames = fields.keySet();
            for (String key : columnNames) {
                setByType(rs, result, key);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            DataAccessException.handleException(e);
        }
        return result;
    }

    private void setByType(ResultSet rs, T object, String targetField) throws SQLException, IllegalAccessException {
        Field field = fields.get(targetField);
        Class fieldType = field.getType();
        field.setAccessible(true);

        if (String.class.equals(fieldType)) {
            field.set(object, rs.getString(targetField));
        }

        if (int.class.equals(fieldType)) {
            field.setInt(object, rs.getInt(targetField));
        }

        if (long.class.equals(fieldType)) {
            field.setLong(object, rs.getLong(targetField));
        }

        if (double.class.equals(fieldType)) {
            field.setDouble(object, rs.getDouble(targetField));
        }
    }
}
