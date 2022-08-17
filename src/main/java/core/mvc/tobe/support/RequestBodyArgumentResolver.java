package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;
import core.util.ReflectionUtils;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestBodyArgumentResolver extends AbstractModelArgumentResolver {

    private RequestParameterUtils parameterUtils;

    @Override
    public boolean supports(final MethodParameter methodParameter) {
        return super.supports(methodParameter) && supportAnnotation(methodParameter);
    }

    private boolean supportAnnotation(MethodParameter methodParameter) {
        return Arrays.stream(methodParameter.getAnnotations())
            .anyMatch(ann -> ann.annotationType() == RequestBody.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter methodParameter, final HttpServletRequest request, final HttpServletResponse response) {
        parameterUtils = new RequestParameterUtils(request);

        if (isSimpleType(methodParameter.getType())) {
            final String parameterValue = getParameter(request, methodParameter.getParameterName());
            return ReflectionUtils.convertStringValue(parameterValue, methodParameter.getType());
        }
        return super.resolveArgument(methodParameter, request, response);
    }

    @Override
    String getParameter(final HttpServletRequest request, final String parameterName) {
        return parameterUtils.getParameter(parameterName);
    }
}
