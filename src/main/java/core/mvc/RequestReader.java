package core.mvc;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestReader {
    public static <T> T fromBody(final ServletRequest request, final Class<T> clazz) throws IOException {
        final String fromBody = IOUtils.toString(request.getReader());
        return (T) JsonUtils.toObject(fromBody, clazz);
    }

    public static String fromQueryString(final HttpServletRequest request, final String parameterName) {
        return request.getParameter(parameterName);
    }
}
