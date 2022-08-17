package core.mvc.tobe.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.JsonUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

public class RequestParameterUtils {

    private static final String QUERY_STRING_KEY_VALUE_DELIMITER = "=";
    private static final String QUERY_STRING_DELIMITER = "&";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ThreadLocal<Map<String, Object>> PARAMETER_THREAD_LOCAL = new ThreadLocal<>();

    private final HttpServletRequest request;
    private final String body;

    public RequestParameterUtils(final HttpServletRequest request) {
        this.request = request;
        try {
            body = request.getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
            if (!body.isBlank()) {
                parseRequestBodyParameters();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void parseRequestBodyParameters() throws JsonProcessingException {
        PARAMETER_THREAD_LOCAL.set(parseRequestBody());
    }

    private Map<String, Object> parseRequestBody() throws JsonProcessingException {
        if (body.contains(QUERY_STRING_KEY_VALUE_DELIMITER)) {
            return Arrays.stream(body.split(QUERY_STRING_DELIMITER))
                .map(s -> s.split(QUERY_STRING_KEY_VALUE_DELIMITER))
                .collect(getMapCollector());
        }
        return OBJECT_MAPPER.readValue(body, new TypeReference<>() {});
    }

    private static Collector<String[], ?, Map<String, Object>> getMapCollector() {
        return Collectors.toMap(
            strings -> strings[0],
            strings -> strings[1]
        );
    }

    public String getParameter(final String parameterName) {
        final String queryParameter = request.getParameter(parameterName);
        if (Objects.isNull(queryParameter)) {
            return getRequestBodyParameter(parameterName);
        }
        return queryParameter;
    }

    private String getRequestBodyParameter(final String parameterName) {
        final String parameter = (String) PARAMETER_THREAD_LOCAL.get().get(parameterName);
        if (Objects.isNull(parameter)) {
            return JsonUtils.getAsStringOrNull(body, parameterName);
        }
        return parameter;
    }

    public static void invalidate() {
        PARAMETER_THREAD_LOCAL.remove();
    }
}
