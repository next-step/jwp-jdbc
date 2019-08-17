package core.mvc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

public class JsonUtils {
    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
    }
    public static <T> T toObject(String json, Class<T> clazz) throws ObjectMapperException {
        try {
            objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new ObjectMapperException(e);
        }
    }

    public static void toJson(ServletOutputStream outputStream, Object object) throws IOException {
        objectMapper.writeValue(outputStream, object);
    }
}
