package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (isEmpty(model)) {
            return;
        }
        writeBody(jsonString(model), response);
    }

    private void writeBody(String body, HttpServletResponse response) throws IOException {
        response.setContentLength(body.length());
        response.getWriter().write(body);
    }

    private String jsonString(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return MAPPER.writeValueAsString(extractFirstValue(model));
        }
        return MAPPER.writeValueAsString(model);
    }

    private Object extractFirstValue(Map<String, ?> model) {
        return model.values().iterator().next();
    }

    private boolean isEmpty(Map<String, ?> model) {
        return model == null || model.isEmpty();
    }
}
