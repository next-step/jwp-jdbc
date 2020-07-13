package core.mvc.tobe.support;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;

public interface MessageConverter {

    boolean canRead(HttpServletRequest request);

    <T> T read(String body, Class<T> targetType);

    void write(OutputStream outputStream, Object object) throws IOException;
}
