package core.mvc.tobe.support;

import javax.servlet.http.HttpServletRequest;

public class ModelArgumentResolver extends AbstractModelArgumentResolver {

    @Override
    protected String getParameter(final HttpServletRequest request, final String parameterName) {
        return request.getParameter(parameterName);
    }

}
