package core.mvc.resolver;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.MethodParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class RequestResponseBodyProcessor extends AbstractBodyProcessor {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseBodyProcessor.class);

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.hasAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        if (methodParameter.hasAnnotation(RequestBody.class)) {
            try {
                String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                return JsonUtils.toObject(json, methodParameter.getParameterType());

            } catch (IOException e) {
                log.error("body processing failed. {}", e);
            }
        }

        return null;
    }
}
