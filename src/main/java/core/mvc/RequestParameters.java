package core.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.CharStreams;
import core.util.ObjectMapperUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParameters {
    private Map<String, Object> parameters = new HashMap<>();

    public RequestParameters(HttpServletRequest request) throws IOException {
        if (request.getHeader("content-type").equals("application/json")) {
            String requestBody = CharStreams.toString(request.getReader());
            if (requestBody.isEmpty()) {
                return;
            }

            this.parameters = ObjectMapperUtils.readValue(requestBody, new TypeReference<Map<String, Object>>() {
            });
            return;
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
            String key = parameterEntry.getKey();
            String[] value = parameterEntry.getValue();

            parameters.put(key, getValue(value));
        }
    }

    private Object getValue(String[] values) {
        if (values.length > 1) {
            return values;
        }
        if (values.length == 1) {
            return values[0];
        }
        return null;
    }

    public <T> T getBodyObject(Class<T> type) {
        return ObjectMapperUtils.convertValue(this.parameters, type);
    }

    public Object getParameter(String name) {
        return parameters.get(name);
    }
}
