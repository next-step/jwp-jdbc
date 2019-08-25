package core.mvc;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletRequest;
import java.io.IOException;

public class RequestReader {
    public static <T> T fromBody(final ServletRequest request, final Class<T> clazz) throws IOException {
        final String fromBody = IOUtils.toString(request.getReader());
        return (T) JsonUtils.toObject(fromBody, clazz);
    }
}
