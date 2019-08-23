package core.mvc.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.ObjectMapperException;

import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public static <T> T toObject(ObjectMapper om, String json, Class<T> clazz) throws ObjectMapperException {
        try {
            return om.readValue(json, clazz);
        } catch (IOException e) {
            throw new ObjectMapperException(e);
        }
    }

    public static String toJson(ObjectMapper om, Object object) {
        try {
            return om.writeValueAsString(object);
        } catch (IOException e) {
            throw new ObjectMapperException(e);
        }
    }
}
