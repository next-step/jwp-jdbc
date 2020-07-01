package core.mvc.tobe.support;

import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return methodParameter.getType() == HttpServletRequest.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return request;
    }
}
