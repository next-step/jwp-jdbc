package core.mvc.tobe.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.RequestBody;
import core.mvc.ObjectMapperException;
import core.mvc.tobe.MethodParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {
    private static final Logger logger = LoggerFactory.getLogger(RequestBodyArgumentResolver.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String NEWLINE = "\n";

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            String body = request.getReader()
                    .lines()
                    .collect(Collectors.joining(NEWLINE));

            return objectMapper.readValue(body, methodParameter.getType());
        } catch (IOException e) {
            logger.error("Fail to convert value to {}", methodParameter.getType());
            throw new ObjectMapperException(e.getCause());
        }
    }
}
