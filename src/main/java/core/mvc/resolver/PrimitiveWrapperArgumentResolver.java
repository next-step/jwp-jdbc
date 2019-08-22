package core.mvc.resolver;

import core.mvc.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrimitiveWrapperArgumentResolver extends AbstractMethodArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        return isPrimitiveOrPrimitiveWrapperType(parameter) || String.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return getValueWithType(methodParameter.getParameterType(), request.getParameter(methodParameter.getParameterName()));
    }

    private boolean isPrimitiveOrPrimitiveWrapperType(MethodParameter parameter) {
        return parameter.getParameterType().isPrimitive() || isPrimitiveWrapperType(parameter.getParameterType());
    }
}
