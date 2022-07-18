package core.mvc.tobe.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            String jsonValue = request.getReader()
                                    .lines()
                                    .collect(Collectors.joining(System.lineSeparator()));
            return objectMapper.readValue(jsonValue, methodParameter.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
