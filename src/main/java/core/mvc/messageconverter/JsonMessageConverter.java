package core.mvc.messageconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.ObjectMapperException;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

public class JsonMessageConverter implements MessageConverter {

    private ObjectMapper objectMapper;

    public JsonMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportedMediaTypes(MediaType type) {
        return MediaType.APPLICATION_JSON_UTF8_VALUE.equals(type.toString());
    }

    @Override
    public <T> T readMessage(String message, Class<T> clazz) throws ObjectMapperException {
        try {
            return objectMapper.readValue(message, clazz);
        } catch (IOException e) {
            throw new ObjectMapperException(e);
        }
    }

    @Override
    public void writeMessage(ServletOutputStream outputStream, Object object) throws IOException {
        objectMapper.writeValue(outputStream, object);
    }
}
