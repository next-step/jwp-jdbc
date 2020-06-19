package core.mvc.tobe.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.tobe.HandlerMethodArgumentResolver;
import core.mvc.tobe.MethodParameter;
import core.mvc.tobe.PathPatternUtils;
import org.apache.commons.lang3.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;


public class RequestParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    public RequestParamHandlerMethodArgumentResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        if (ClassUtils.isPrimitiveOrWrapper(parameter.getParameterType())) {
            return PathPatternUtils.toPrimitive(parameter.getParameterType(), request.getParameter(parameter.getName()));
        }

        if (String.class.equals(parameter.getParameterType())) {
            return request.getParameter(parameter.getName());
        }

        Map<String, Object> parameterMap = request.getParameterMap().keySet().stream()
                .collect(Collectors.toMap(key -> key, key -> request.getParameterMap().get(key)[0]));
        Object object = objectMapper.convertValue(parameterMap, parameter.getParameterType());

        return object;
    }
}
