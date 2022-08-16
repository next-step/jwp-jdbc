package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.MethodArgumentTypeNotSupportedException;
import core.mvc.ObjectMapperException;
import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> clazz = methodParameter.getType();

        String body = "";
        try (BufferedReader reader = request.getReader()) {
            body = reader.lines()
                    .collect(Collectors.joining(""));
            return JsonUtils.toObject(body, clazz);
        } catch (ObjectMapperException e) {
            throw new MethodArgumentTypeNotSupportedException(methodParameter.getType(), body);
        } catch (IOException e) {
            throw new IllegalStateException(methodParameter.getType() + " 값 읽기를 실패했습니다.", e);
        }
    }
}
