package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        if (model.size() == 1) {
            parseSingleResponse(model, response);
            return;
        }

        objectMapper.writeValue(response.getOutputStream(), model);
    }

    private void parseSingleResponse(Map<String, ?> model, HttpServletResponse response) throws IOException {
        String key = model.keySet()
                        .stream()
                        .findAny()
                        .get();

        objectMapper.writeValue(response.getOutputStream(), model.get(key));
    }
}
