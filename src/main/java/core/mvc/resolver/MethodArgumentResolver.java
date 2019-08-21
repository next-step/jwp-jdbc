package core.mvc.resolver;

import core.mvc.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MethodArgumentResolver {
    boolean supports(MethodParameter parameter);

    Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response);
}
