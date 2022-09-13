package core.mvc.tobe.support;

import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;

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
        String content = null;
        try {
            content = request.getReader()
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            return JsonUtils.toObject(content, methodParameter.getType());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
}
