package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (model.isEmpty()) {
            return;
        }

        if (model.size() == 1) {
            objectMapper.writeValue(response.getOutputStream(), model.values().stream().findFirst().get());
            return;
        }

        objectMapper.writeValue(response.getOutputStream(), model);
    }
}
