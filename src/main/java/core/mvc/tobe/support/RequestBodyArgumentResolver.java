package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    @Override
    public boolean supports(final MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter methodParameter, final HttpServletRequest request, final HttpServletResponse response) {
        return null;
    }
}
