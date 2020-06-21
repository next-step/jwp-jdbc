package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        final String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            throw new IllegalArgumentException("Unable to parse body except for application/json");
        }

        try {
            final String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            return JsonUtils.toObject(body, methodParameter.getType());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read body.");
        }
    }
}
