package core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.exception.ExceptionStatus;
import core.exception.JdbcException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectMapperUtils {
    private ObjectMapper objectMapper = new ObjectMapper();

    public <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new JdbcException(ExceptionStatus.JSON_PARSE_EXCEPTION);
        }
    }
}
