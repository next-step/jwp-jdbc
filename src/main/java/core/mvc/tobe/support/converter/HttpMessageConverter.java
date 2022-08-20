package core.mvc.tobe.support.converter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;

public interface HttpMessageConverter {

    boolean canRead(Class<?> clazz, MediaType mediaType);

    Object read(Class<?> clazz, HttpInputMessage inputMessage);
}
