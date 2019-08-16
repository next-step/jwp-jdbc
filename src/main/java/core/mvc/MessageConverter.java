package core.mvc;

import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;

public interface MessageConverter {

    boolean support(MediaType mediaType);

    void write(Object value, OutputStream outputStream) throws IOException;
}
