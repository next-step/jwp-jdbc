package core.mvc.tobe.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.JsonUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

public class RequestParameterUtils {

    private static final String QUERY_STRING_KEY_VALUE_DELIMITER = "=";
    private static final String QUERY_STRING_DELIMITER = "&";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Map<String, Object> parameters = new LinkedHashMap<>();
    private final HttpServletRequest request;
    private final String body;

    public RequestParameterUtils(final HttpServletRequest request) {
        this.request = request;
        this.body = getRequestBodyData(request);
    }

    private String getRequestBodyData(final HttpServletRequest request) {
        try {
            final String bodyData = parseBodyData(request);
            parseRequestBodyData(bodyData);
            return bodyData;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String parseBodyData(final HttpServletRequest request) throws IOException {
        final BufferedReader reader = request.getReader();
        reader.mark(1024 * 1024);
        final String bodyData = reader.lines()
            .collect(Collectors.joining(System.lineSeparator()));
        reader.reset();
        return bodyData;
    }

    private void parseRequestBodyData(final String bodyData) throws JsonProcessingException {
        if (!bodyData.isBlank()) {
            parameters.putAll(parseRequestBody(bodyData));
        }
    }

    private Map<String, Object> parseRequestBody(final String bodyData) throws JsonProcessingException {
        if (bodyData.contains(QUERY_STRING_KEY_VALUE_DELIMITER)) {
            return Arrays.stream(bodyData.split(QUERY_STRING_DELIMITER))
                .map(s -> s.split(QUERY_STRING_KEY_VALUE_DELIMITER))
                .collect(getMapCollector());
        }
        return OBJECT_MAPPER.readValue(bodyData, new TypeReference<>() {
        });
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
        final String parameter = (String) parameters.get(parameterName);
        if (Objects.isNull(parameter)) {
            return JsonUtils.getAsStringOrNull(body, parameterName);
        }
        return parameter;
    }

}
