package core.mvc.messageconverter;

import core.mvc.ObjectMapperException;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

public interface MessageConverter {
    boolean supportedMediaTypes(MediaType type);
    <T> T readMessage(String message, Class<T> clazz) throws ObjectMapperException;
    void writeMessage(ServletOutputStream outputStream, Object object) throws IOException;
}
