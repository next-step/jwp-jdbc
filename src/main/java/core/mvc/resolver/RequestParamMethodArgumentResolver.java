package core.mvc.resolver;

import core.annotation.web.RequestParam;
import core.mvc.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestParamMethodArgumentResolver extends AbstractMethodArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.hasAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {

        RequestParam requestParam = methodParameter.getAnnotation(RequestParam.class);

        Class<?> parameterType = methodParameter.getParameterType();

        if (requestParam == null || requestParam.value().isEmpty()) {
            return getValueWithType(parameterType, request.getParameter(methodParameter.getParameterName()));
        }

        return getValueWithType(parameterType, request.getParameter(requestParam.value()));
    }
}
