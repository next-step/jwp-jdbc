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
        try {
            return resolve(methodParameter, request, response);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("유효하지 않은 인자입니다. %s", e.getMessage()));
        }
    }

    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Class<?> clazz = methodParameter.getType();
        String readLine = request.getReader().readLine();
        return JsonUtils.toObject(readLine, clazz);
    }
}
