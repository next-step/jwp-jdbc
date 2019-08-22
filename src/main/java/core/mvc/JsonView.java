package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class JsonView implements View {

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    private ObjectMapper objectMapper;

    public JsonView() {
        this(DEFAULT_OBJECT_MAPPER);
    }

    public JsonView(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        writeBody(model, response);
    }

    private void writeBody(Map<String, ?> model, HttpServletResponse response) throws IOException {
        if (Objects.isNull(model) || model.isEmpty()) {
            return;
        }

        Object target = parseModel(model);
        objectMapper.writeValue(response.getWriter(), target);
    }

    private Object parseModel(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().stream()
                    .findFirst()
                    .get();
        }
        return model;
    }
}
