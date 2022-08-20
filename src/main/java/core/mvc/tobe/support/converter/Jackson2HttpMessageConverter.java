package core.mvc.tobe.support.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Jackson2HttpMessageConverter implements HttpMessageConverter {

    private static final Jackson2HttpMessageConverter INSTANCE = new Jackson2HttpMessageConverter();
    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();
    private static final List<MediaType> DEFAULT_SUPPORT_MEDIA_TYPES = List.of(MediaType.APPLICATION_JSON);

    private Jackson2HttpMessageConverter() {
        if (INSTANCE != null) {
            throw new AssertionError(String.format("%s can not be instanced", getClass()));
        }
    }

    public static Jackson2HttpMessageConverter instance() {
        return INSTANCE;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return isSupportedMediaType(mediaType);
    }

    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage) {
        try {
            return DEFAULT_OBJECT_MAPPER.readValue(
                    new InputStreamReader(inputMessage.getBody()), clazz);
        } catch (IOException e) {
            throw new IllegalStateException("could not read request message", e);
        }
    }

    private boolean isSupportedMediaType(MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        return DEFAULT_SUPPORT_MEDIA_TYPES.stream().anyMatch(type -> type.includes(mediaType));
    }
}
