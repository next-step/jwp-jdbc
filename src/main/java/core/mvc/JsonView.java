package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JsonView implements View {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int SINGLE_MODEL = 1;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (model.isEmpty()) {
            return;
        }

        if (isSingleModel(model)) {
            renderSingleModel(model, response);
            return;
        }

        renderMultipleModels(model, response);
    }

    private boolean isSingleModel(Map<String, ?> model) {
        return model.size() == SINGLE_MODEL;
    }

    private void renderSingleModel(Map<String, ?> model, HttpServletResponse response) throws IOException {
        final Object value = model.values()
                .stream()
                .findFirst()
                .orElse(null);
        OBJECT_MAPPER.writeValue(response.getOutputStream(), value);
    }

    private void renderMultipleModels(Map<String, ?> model, HttpServletResponse response) throws IOException {
        OBJECT_MAPPER.writeValue(response.getOutputStream(), model);
    }
}
