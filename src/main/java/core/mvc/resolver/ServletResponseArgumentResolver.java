package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletResponseArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.getType().equals(HttpServletResponse.class);
    }

    @Override
    public Object getMethodArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return response;
    }
}
