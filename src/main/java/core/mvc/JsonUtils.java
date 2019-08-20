package core.mvc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

import java.io.IOException;
import java.io.InputStream;

public final class JsonUtils {

    private JsonUtils() { }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        final VisibilityChecker<?> visibilityChecker = objectMapper.getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE);

        objectMapper.setVisibility(visibilityChecker);
    }

    public static <T> T toObject(final String value,
                                 final Class<T> clazz) throws ObjectMapperException {
        try {
            return objectMapper.readValue(value, clazz);
        } catch (final IOException e) {
            throw new ObjectMapperException(e);
        }
    }

    public static <T> T toObject(final InputStream value,
                                 final Class<T> clazz) throws ObjectMapperException {
        try {
            return objectMapper.readValue(value, clazz);
        } catch (final IOException e) {
            throw new ObjectMapperException(e);
        }
    }
}
