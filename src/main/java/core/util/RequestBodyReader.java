package core.util;

import core.mvc.JsonUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestBodyReader {

    public static <T> T read(HttpServletRequest request, Class<T> clazz) throws IOException {
        String body = IOUtils.toString(request.getReader());

        return JsonUtils.toObject(body, clazz);
    }
}
