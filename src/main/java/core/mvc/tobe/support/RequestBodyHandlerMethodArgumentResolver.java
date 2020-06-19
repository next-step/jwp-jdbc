package core.mvc.tobe.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.RequestBody;
import core.mvc.tobe.HandlerMethodArgumentResolver;
import core.mvc.tobe.MethodParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author KingCjy
 */
public class RequestBodyHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(RequestBodyHandlerMethodArgumentResolver.class);
    private final ObjectMapper objectMapper;

    public RequestBodyHandlerMethodArgumentResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestBody.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        try {
            return objectMapper.readValue(request.getReader(), parameter.getParameterType());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
