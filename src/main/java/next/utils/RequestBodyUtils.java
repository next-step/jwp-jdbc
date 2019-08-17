package next.utils;

import core.mvc.JsonUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

public class RequestBodyUtils {
    public static Object toObject(HttpServletRequest request, Class<?> clazz) throws IOException {
        String body = IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
        return JsonUtils.toObject(body, clazz);
    }
}
