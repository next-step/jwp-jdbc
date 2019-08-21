package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;
import next.utils.RequestBodyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestBodyArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.isRequestBody();
    }

    @Override
    public Object getMethodArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return RequestBodyUtils.toObject(request, parameter.getType());
    }
}
