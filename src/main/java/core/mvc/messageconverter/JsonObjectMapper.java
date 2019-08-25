package core.mvc.messageconverter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectMapper {

    private ObjectMapper objectMapper;

    private JsonObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public static JsonObjectMapper jsonObjectMapperBuilder() {
        return new JsonObjectMapper();
    }

    public ObjectMapper build() {
        return objectMapper;
    }
}

