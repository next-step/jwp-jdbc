package core.mvc;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class HttpServletRequestUtils {

    public static String getRequestBody(final HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
