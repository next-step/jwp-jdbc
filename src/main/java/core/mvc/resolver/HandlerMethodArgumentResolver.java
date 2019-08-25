package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodArgumentResolver {
    boolean supports(MethodParameter parameter);
    Object resolveMethodArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
