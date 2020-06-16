package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class JsonView implements View {
    public static final String CONTENT_TYPE = "Content-Type";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setResponseHeader(response);
        setResponseStatus(response);
        writeBody(model, response);
    }

    private void setResponseHeader(HttpServletResponse response) {
        response.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    private void setResponseStatus(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void writeBody(Map<String, ?> model, HttpServletResponse response) throws IOException {
        String body = "";

        Object target = getTarget(model);

        if (Objects.nonNull(target)) {
            body = objectMapper.writeValueAsString(target);
        }

        response.getWriter().write(body);
        response.getWriter().flush();
        response.getWriter().close();
    }

    private Object getTarget(Map<String, ?> model) {
        if (Objects.isNull(model) || model.isEmpty()) {
            return null;
        }

        if (model.size() == 1) {
            return getFirstValue(model);
        }

        return model;
    }

    private Object getFirstValue(Map<String, ?> model) {
        return model.values()
            .stream()
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }
}
