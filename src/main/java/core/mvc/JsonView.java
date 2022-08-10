package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (model.isEmpty()) {
            return;
        }
        String body = model.size() == 1
                ? renderOneElement(model)
                : renderElements(model);
        response.setContentLength(body.getBytes(StandardCharsets.UTF_8).length);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(body);
            writer.flush();
        }
    }

    private String renderOneElement(Map<String, ?> model) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                model.values().iterator().next()
        );
    }

    private String renderElements(Map<String, ?> model) throws JsonProcessingException {
        return objectMapper.writeValueAsString(model);
    }
}
