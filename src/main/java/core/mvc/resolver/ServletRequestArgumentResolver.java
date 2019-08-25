package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRequestArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.getType().equals(HttpServletRequest.class);
    }

    @Override
    public Object resolveMethodArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return request;
    }
}
