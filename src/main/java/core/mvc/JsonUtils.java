package core.mvc;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private JsonUtils() {
    }

    public static <T> T toObject(String json, Class<T> clazz) throws ObjectMapperException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new ObjectMapperException(e);
        }
    }
}
