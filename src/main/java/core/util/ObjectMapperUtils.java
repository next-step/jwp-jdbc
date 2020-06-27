package core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.exception.ExceptionStatus;
import core.exception.JdbcException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectMapperUtils {
    private ObjectMapper mapper = new ObjectMapper();

    public <T> T convertValue(Object object, Class<T> clazz) {
        return mapper.convertValue(object, clazz);
    }

    public <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            //test
            System.out.println("test123 : " + content);

            return mapper.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            //test
            e.printStackTrace();

            throw new JdbcException(ExceptionStatus.JSON_PARSE_EXCEPTION);
        }
    }
}
