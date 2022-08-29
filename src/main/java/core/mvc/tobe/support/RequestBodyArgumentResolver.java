package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;
import next.support.ObjectMapperFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            Class<?> parameterClass = methodParameter.getType();
            return ObjectMapperFactory.getInstance().readValue(requestBody, parameterClass);
        } catch (Exception exception) {
            throw new IllegalArgumentException();
        }
    }

}
