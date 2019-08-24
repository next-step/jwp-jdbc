package core.mvc.resolver;

import core.exception.NotSupportedException;
import core.mvc.messageconverter.MessageConverter;
import core.mvc.tobe.MethodParameter;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.List;

public class RequestBodyArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    private List<MessageConverter> converters;

    public RequestBodyArgumentResolver(List<MessageConverter> converters) {
        this.converters = converters;
    }

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.isRequestBody();
    }

    @Override
    public Object getMethodArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
        MessageConverter converter = converters.stream()
                .filter(c -> c.supportedMediaTypes(MediaType.valueOf(request.getHeader(HttpHeaders.CONTENT_TYPE))))
                .findFirst()
                .orElseThrow(NotSupportedException::new);

        return converter.readMessage(body, parameter.getType());
    }
}
