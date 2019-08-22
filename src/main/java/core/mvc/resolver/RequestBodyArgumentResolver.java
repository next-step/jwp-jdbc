package core.mvc.resolver;

import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

public class RequestBodyArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.isRequestBody();
    }

    @Override
    public Object getMethodArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
        return JsonUtils.toObject(body, parameter.getType());
    }
}
