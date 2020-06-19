package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMethodArgumentResolver {
    boolean supportsParameter(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter, HttpServletRequest httpServletRequest);
}
