package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String jsonString = extractJsonString(request);
        return convertJsonStringToObject(jsonString, methodParameter.getType());
    }

    private String extractJsonString(HttpServletRequest request) {
        try {
            return request.getReader().readLine();
        } catch (IOException e) {
            throw new IllegalArgumentException("Request body is not valid to extract json string");
        }
    }

    private Object convertJsonStringToObject(String jsonString, Class<?> type) {
        return JsonUtils.toObject(jsonString, type);
    }
}
