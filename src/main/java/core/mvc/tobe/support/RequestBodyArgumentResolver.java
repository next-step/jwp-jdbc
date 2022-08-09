package core.mvc.tobe.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            return resolveArgument(methodParameter, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) throws IOException {
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        if (StringUtils.isBlank(body)) {
            return null;
        }
        Class<?> type = methodParameter.getType();
        return OBJECT_MAPPER.readValue(body, type);
    }
}
