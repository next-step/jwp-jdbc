package core.mvc;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

public class RequestBodyParser {

    public static <T> T parse(HttpServletRequest request, Class<T> requestClass) throws Exception {
        String requestBody = request.getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        return JsonUtils.toObject(requestBody, requestClass);
    }
}
