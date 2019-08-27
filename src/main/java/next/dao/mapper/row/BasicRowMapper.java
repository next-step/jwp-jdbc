package next.dao.mapper.row;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

public class BasicRowMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(BasicRowMapper.class);

    private Map<String, Field> fields;
    private List<String> targetFields;

    protected BasicRowMapper(Class<T> clazz, String... targetFields) {
        initFields(clazz);
        initTargetFields(targetFields);
    }

    private void initFields(Class<T> clazz) {
        fields = new HashMap<>();
        Stream.of(clazz.getDeclaredFields())
                .forEach(field -> fields.put(field.getName(), field));
    }

    private void initTargetFields(String[] targetFields) {
        this.targetFields = new ArrayList<>();
        Collections.addAll(this.targetFields, targetFields);
    }

    protected List<T> mapObject(ResultSet rs, List<T> result, Class<T> clazz) {
        try {
            while (rs.next()) {
                result.add(addOne(rs, clazz.newInstance()));
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    private T addOne(ResultSet rs, T object) throws SQLException, IllegalAccessException {
        for (String targetField : targetFields) {
            setByType(rs, object, targetField);
        }

        return object;
    }

    private void setByType(ResultSet rs, T object, String targetField) throws IllegalAccessException, SQLException {
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
