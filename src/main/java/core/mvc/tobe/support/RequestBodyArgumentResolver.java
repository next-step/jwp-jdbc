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
            Class<?> parameterClazz = methodParameter.getType();
            String readLine = request.getReader().readLine();

            return JsonUtils.toObject(readLine, parameterClazz);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Request Parameter....");
        }
    }
}
