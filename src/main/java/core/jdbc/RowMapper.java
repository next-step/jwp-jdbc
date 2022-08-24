package core.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.jdbc.exception.MappingException;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

final class RowMapper<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Class<T> objectType;

    RowMapper(Class<T> objectType) {
        Assert.notNull(objectType, "'objectType' must not be null");
        this.objectType = objectType;
    }

    T map(ResultSet resultSet) throws SQLException {
        try {
            Map<String, Object> value = new HashMap<>();
            for (Field field : objectType.getDeclaredFields()) {
                value.put(field.getName(), resultSet.getObject(field.getName()));
            }
            return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(value), objectType);
        } catch (JsonProcessingException e) {
            throw new MappingException(String.format("could not instantiate entity %s", objectType), e);
        }
    }
}
