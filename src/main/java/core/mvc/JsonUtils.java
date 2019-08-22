package core.mvc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setVisibility(OBJECT_MAPPER.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public static <T> T toObject(String json, Class<T> clazz) throws ObjectMapperException {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new ObjectMapperException(e);
        }
    }

    public static <T> T toObject(Reader reader, Class<T> clazz) throws ObjectMapperException {
        try {
            return OBJECT_MAPPER.readValue(reader, clazz);
        } catch (IOException e) {
            throw new ObjectMapperException(e);
        }
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) throws ObjectMapperException {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new ObjectMapperException(e);
        }
    }

    public static String convertToJsonString(Object model) {
        try {
            return OBJECT_MAPPER.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new ObjectMapperException(e);
        }
    }

}
