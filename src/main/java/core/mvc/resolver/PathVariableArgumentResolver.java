package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.MethodParameter;
import core.mvc.utils.PathPatternUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public class PathVariableArgumentResolver extends AbstractMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.hasAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        PathVariable pathVariable = methodParameter.getAnnotation(PathVariable.class);

        Map<String, String> uriVariables = getPathVariables(methodParameter.getMethod(), request);

        if (pathVariable.value().isEmpty()) {
            return getValueWithType(methodParameter.getParameterType(), uriVariables.get(methodParameter.getParameterName()));
        }

        return getValueWithType(methodParameter.getParameterType(), uriVariables.get(pathVariable.value()));
    }

    private Map<String, String> getPathVariables(Method method, HttpServletRequest request) {
        String path = getDeclaredPath(method);
        return PathPatternUtils.extractUriVariables(path, request.getRequestURI());
    }

    private String getDeclaredPath(Method method) {
        RequestMapping parentRequestMapping = method.getDeclaringClass().getAnnotation(RequestMapping.class);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        if(parentRequestMapping == null) {
            return requestMapping.value();
        }

        return parentRequestMapping.value() + requestMapping.value();
    }
}
