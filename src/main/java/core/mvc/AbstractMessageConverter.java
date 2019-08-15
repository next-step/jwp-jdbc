package core.mvc;

import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractMessageConverter implements MessageConverter {

    private final MediaType mediaType;

    public AbstractMessageConverter(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public boolean support(MediaType mediaType) {
        return this.mediaType.equals(mediaType);
    }

    public abstract void write(Object value, OutputStream outputStream) throws IOException;
}
