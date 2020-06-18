package core.mvc.tobe.support;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.HandlerMethodArgumentResolver;
import core.mvc.tobe.MethodParameter;
import core.mvc.tobe.PathPatternUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PathVariableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private Map<String, PathPattern> pathPatternCache = new HashMap<>();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(PathVariable.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        String path = getFullPath(parameter.getMethod());
        String requestPath = request.getRequestURI();

        PathPattern pathPattern = pathPatternCache.get(path);

        if (pathPattern == null) {
            pathPattern = PathPatternUtils.parse(path);
            pathPatternCache.put(path, pathPattern);
        }

        PathContainer pathContainer = PathPatternUtils.toPathContainer(requestPath);
        Map<String, String> variables = pathPattern.matchAndExtract(pathContainer).getUriVariables();

        return PathPatternUtils.toPrimitive(parameter.getParameterType(), variables.get(parameter.getName()));
    }

    private String getFullPath(Method method) {
        RequestMapping controllerMapping = method.getDeclaringClass().getAnnotation(RequestMapping.class);
        RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);

        return controllerMapping == null ? methodMapping.value() : controllerMapping.value() + methodMapping.value();
    }
}
