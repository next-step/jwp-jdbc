package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;
import core.util.IOUtils;
import org.slf4j.Logger;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    private static final Logger logger = getLogger(RequestBodyArgumentResolver.class);

    private final List<MessageConverter> messageConverters;

    public RequestBodyArgumentResolver(List<MessageConverter> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rawBody = IOUtils.readData(request.getReader(), request.getContentLength());
        return getBody(methodParameter, request, rawBody);
    }

    private Object getBody(MethodParameter methodParameter, HttpServletRequest request, String rawBody) {
        for (MessageConverter messageConverter : this.messageConverters) {
            if (messageConverter.canRead(request)) {
                return messageConverter.read(rawBody, methodParameter.getType());
            }
        }
        throw new IllegalStateException("No suitable converter for request: " + methodParameter.getType());
    }
}
