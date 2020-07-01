package core.mvc.tobe.support;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.MethodParameter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static core.util.PathPatternUtil.getUriValue;

public class PathVariableArgumentResolver extends AbstractAnnotationArgumentResolver {

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        PathVariable pathVariable = getAnnotation(methodParameter, PathVariable.class);
        String pattern = getPattern(methodParameter);
        String key = getPathVariableKey(pathVariable, methodParameter.getParameterName());
        return getUriValue(pattern, request.getRequestURI(), key);
    }

    private String getPathVariableKey(PathVariable pathVariable, String parameterName) {
        return StringUtils.isNotBlank(pathVariable.name()) ? pathVariable.name()
                : StringUtils.isNotBlank(pathVariable.value()) ? pathVariable.value()
                : parameterName;
    }

    private String getPattern(MethodParameter methodParameter) {
        Method method = methodParameter.getMethod();
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            return annotation.value();
        }

        throw new IllegalStateException(method.getName() + " doesn't have RequestMapping annotation");
    }
}
