package core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryStringUtil {

    private static final String QUERY_STRING_DELIMITER = "?";
    private static final String QUERY_PARAMETER_DELIMITER = "&";
    private static final String QUERY_KEY_VALUE_DELIMITER = "=";

    public static String generateQueryStringWithUri(String uri, Map<String, String> parameters) {
        return new StringBuilder()
                .append(uri)
                .append(QUERY_STRING_DELIMITER)
                .append(generateQueryString(parameters))
                .toString();
    }

    public static String generateQueryString(Map<String, String> parameters) {
        return parameters.entrySet().stream()
                .map(entry -> generateQueryParameter(entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(QUERY_PARAMETER_DELIMITER));
    }

    private static String generateQueryParameter(String key, String value) {
        return new StringBuilder()
                .append(key)
                .append(QUERY_KEY_VALUE_DELIMITER)
                .append(value)
                .toString();
    }
}
