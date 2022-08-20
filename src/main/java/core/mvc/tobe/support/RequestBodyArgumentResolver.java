package core.mvc.tobe.support;

import core.mvc.tobe.MethodParameter;
import core.mvc.tobe.support.converter.HttpMessageConverter;
import core.mvc.tobe.support.converter.Jackson2HttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    private static final RequestBodyArgumentResolver RESOLVER_WITH_DEFAULT_CONVERTERS = from(List.of(Jackson2HttpMessageConverter.instance()));
    private final Collection<HttpMessageConverter> converters;

    private RequestBodyArgumentResolver(Collection<HttpMessageConverter> converters) {
        Assert.notNull(converters, "'messageConverters' must not be null");
        this.converters = Collections.unmodifiableCollection(converters);
    }

    public static RequestBodyArgumentResolver from(Collection<HttpMessageConverter> converters) {
        return new RequestBodyArgumentResolver(converters);
    }

    public static RequestBodyArgumentResolver defaults() {
        return RESOLVER_WITH_DEFAULT_CONVERTERS;
    }

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        ServletServerHttpRequest httpRequest = new ServletServerHttpRequest(request);

        MediaType mediaType = httpRequest.getHeaders().getContentType();
        Optional<?> bodyOptional = converters.stream()
                .filter(converter -> converter.canRead(parameter.getType(), mediaType))
                .map(converter -> converter.read(parameter.getType(), httpRequest))
                .findAny();

        RequestBody requestBody = getAnnotation(parameter, RequestBody.class);
        if (requestBody.required() && bodyOptional.isEmpty()) {
            throw new UnsupportedOperationException(
                    String.format("media type(%s) is not supported", mediaType));
        }
        return bodyOptional.orElse(null);
    }
}
