package core.mvc.tobe.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            return resolveArgument(methodParameter, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        String body = getRequestBody(request.getInputStream(), contentLength);

        if (StringUtils.isBlank(body)) {
            return null;
        }

        return JsonUtils.toObject(body, methodParameter.getType());
    }

    private String getRequestBody(InputStream inputStream, int contentLength) throws IOException {
        StringBuilder out = new StringBuilder(contentLength);
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        char[] buffer = new char[contentLength];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            out.append(buffer, 0, read);
        }
        return out.toString();
    }
}
