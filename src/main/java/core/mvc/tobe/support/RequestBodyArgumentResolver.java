package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestBodyArgumentResolver extends AbstractModelArgumentResolver {

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
        return null;
    }

    @Override
    String getParameter(final HttpServletRequest request, final String parameterName) {
        return null;
    }
}
