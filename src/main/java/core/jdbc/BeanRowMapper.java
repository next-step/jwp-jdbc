package core.jdbc;

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
    private final T instance;
    private Set<String> columnNames;

    public BeanRowMapper(Class<T> type) {
        this(type, true);
    }

    public BeanRowMapper(Class<T> type, boolean convertToUnderscore) {
        this.type = type;
        this.convertToUnderscore = convertToUnderscore;
        instance = BeanUtils.instantiateClass(type);
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        getColumnNames(rs);
        setResults(rs);
        return instance;
    }

    private void setResults(ResultSet rs) throws SQLException {
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            setField(rs, field);
        }
    }

    private Set<String> getColumnNames(ResultSet rs) throws SQLException {
        this.columnNames = new HashSet<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        return columnNames;
    }

    private void setField(ResultSet rs, Field field) throws SQLException {
        String fieldName = getFieldName(field);
        if (isContainsColumnName(fieldName)) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, instance, getValue(rs, field, fieldName));
        }
    }

    private boolean isContainsColumnName(String fieldName) {
        return columnNames.contains(fieldName) || columnNames.contains(fieldName.toUpperCase());
    }

    private String getFieldName(Field field) {
        String fieldName = field.getName();
        if (!convertToUnderscore) {
            return fieldName;
        }
        return UnderscoreConverter.convertToUnderscore(fieldName);
    }

    private Object getValue(ResultSet rs, Field field, String fieldName) throws SQLException {
        Object value = rs.getObject(fieldName, field.getType());
        if (Objects.nonNull(value)) {
            return value;
        }
        return rs.getObject(fieldName.toUpperCase(), field.getType());
    }
}
