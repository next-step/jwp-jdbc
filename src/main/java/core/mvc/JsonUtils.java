package core.mvc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.function.Function;

public class JsonUtils {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public static <T> T readValue(String json, Class<T> clazz) throws ObjectMapperException {
        return Optional.of(objectMapper)
                .map(wrapper(mapper -> mapper.readValue(json, clazz))).get();
    }

    public static <T> T readValue(Reader reader, Class<T> clazz) {
        return Optional.of(objectMapper)
                .map(wrapper(mapper -> mapper.readValue(reader, clazz))).get();
    }

    public static String writeValue(Object value) throws ObjectMapperException {
        return Optional.of(objectMapper)
                .map(wrapper(mapper -> mapper.writeValueAsString(value))).get();
    }

    @FunctionalInterface
    public interface FunctionWithException<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    private static <T, R, E extends IOException> Function<T, R> wrapper(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (IOException e) {
                throw new ObjectMapperException(e);
            }
        };
    }
}
