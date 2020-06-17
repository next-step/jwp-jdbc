package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class JsonView implements View {
    public static final String CONTENT_TYPE = "Content-Type";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
        setResponseHeader(response);
        getResponseStatus(response);
        writeBody(model, response);
    }

    private void setResponseHeader(HttpServletResponse response) {
        response.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    private void getResponseStatus(HttpServletResponse response) {
        log.debug("status: {}", response.getStatus());
    }

    private void writeBody(Map<String, ?> model, HttpServletResponse response) {
        String body = "";

        Object target = getTarget(model);

        if (Objects.isNull(target)) {
            return;
        }

        try {
            body = objectMapper.writeValueAsString(target);

            log.debug("responseBody: {}", body);

            PrintWriter writer = response.getWriter();
            writer.write(body);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            log.error(e.getMessage());
        }
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
