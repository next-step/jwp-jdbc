package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return JsonUtils.toObject(parseBody(request), methodParameter.getType());
    }

    private String parseBody(HttpServletRequest request) {
        try {
            byte[] body = IOUtils.toByteArray(request.getInputStream());
            return new String(body, request.getCharacterEncoding());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
