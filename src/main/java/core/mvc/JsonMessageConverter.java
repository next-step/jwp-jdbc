package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;

public class JsonMessageConverter extends AbstractMessageConverter{

    private final ObjectMapper objectMapper;

    public JsonMessageConverter() {
        this(new ObjectMapper());
    }

    public JsonMessageConverter(ObjectMapper objectMapper) {
        super(MediaType.APPLICATION_JSON_UTF8);
        this.objectMapper = objectMapper;
    }

    @Override
    public void write(Object value, OutputStream outputStream) throws IOException {
        this.objectMapper.writeValue(outputStream, value);
    }
}
