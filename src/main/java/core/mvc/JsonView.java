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
    private static final int SINGLE_SIZE = 1;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (model.isEmpty()) {
            return;
        }
        try (PrintWriter responseWriter = response.getWriter()) {
            String jsonString = getJsonString(model);
            response.setContentLength(jsonString.getBytes(StandardCharsets.UTF_8).length);
            responseWriter.write(jsonString);
        }
    }

    private String getJsonString(Map<String, ?> model) throws JsonProcessingException {
        if (isSingleModel(model)) {
            Object modelValue = model.values().stream()
                    .findFirst().get();
            return objectMapper.writeValueAsString(modelValue);
        }
        return objectMapper.writeValueAsString(model);
    }

    private boolean isSingleModel(Map<String, ?> model) {
        return model.size() == SINGLE_SIZE;
    }
}
