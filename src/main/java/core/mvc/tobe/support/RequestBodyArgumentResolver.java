package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;

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
        try {
            final String requestBody = request.getReader().lines().collect(Collectors.joining());
            return JsonUtils.toObject(requestBody, methodParameter.getType());
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
