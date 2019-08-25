package core.jdbc;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BeanRowMapper<T> implements RowMapper<T> {

    private final Class<T> type;
    private final boolean convertToUnderscore;

    public BeanRowMapper(Class<T> type) {
        this(type, true);
    }

    public BeanRowMapper(Class<T> type, boolean convertToUnderscore) {
        this.type = type;
        this.convertToUnderscore = convertToUnderscore;
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        T instance = BeanUtils.instantiateClass(type);
        setResults(rs, instance);
        return instance;
    }

    private Set<String> getColumnNames(ResultSet rs) throws SQLException {
        Set<String> columnNames = new HashSet<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        return columnNames;
    }

    private void setResults(ResultSet rs, T instance) throws SQLException {
        Set<String> columnNames = getColumnNames(rs);
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            setResult(rs, field, instance, columnNames);
        }
    }
    private void setResult(ResultSet rs, Field field, T instance, Set<String> columnNames) throws SQLException {
        String columnName = getColumnName(field);
        if (hasColumnName(columnNames, columnName)) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, instance, getValue(rs, field, columnName));
        }
    }

    private String getColumnName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            return column.value();
        }

        String fieldName = field.getName();
        if (!convertToUnderscore) {
            return fieldName;
        }
        return UnderscoreConverter.convertToUnderscore(fieldName);
    }

    private boolean hasColumnName(Set<String> columnNames, String fieldName) {
        return columnNames.contains(fieldName) || columnNames.contains(fieldName.toUpperCase());
    }

    private Object getValue(ResultSet rs, Field field, String columnName) throws SQLException {
        Object value = rs.getObject(columnName, getType(field));
        if (Objects.nonNull(value)) {
            return value;
        }
        return rs.getObject(columnName.toUpperCase(), field.getType());
    }

    private Class<?> getType(Field field) {
        Class<?> type = field.getType();
        if (type.isPrimitive()) {
            return ClassUtils.primitiveToWrapper(type);
        }
        return type;
    }
}
