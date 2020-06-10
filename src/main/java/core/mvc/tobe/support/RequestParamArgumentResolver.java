package core.mvc.tobe.support;

import core.annotation.web.RequestParam;
import core.mvc.MethodArgumentTypeNotSupportedException;
import core.mvc.tobe.MethodParameter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestParamArgumentResolver extends AbstractAnnotationArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        RequestParam requestParam = getAnnotation(methodParameter, RequestParam.class);
        String key = getRequestParamKey(requestParam, methodParameter.getParameterName());

        Object arg = request.getParameter(key);

        if (methodParameter.isString()) {
            return arg;
        } else if (methodParameter.isInteger()) {
            return Integer.valueOf(arg.toString());
        }

        throw new MethodArgumentTypeNotSupportedException(methodParameter.getType(), arg);
    }

    private String getRequestParamKey(RequestParam requestParam, String parameterName) {
        return StringUtils.isNotBlank(requestParam.name()) ? requestParam.name()
                : StringUtils.isNotBlank(requestParam.value()) ? requestParam.value()
                : parameterName;
    }

}
