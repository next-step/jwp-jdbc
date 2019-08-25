package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (model.size() == 0) {
            return;
        }
        objectMapper.writeValue(response.getWriter(), getValue(model));
    }

    private Object getValue(Map<String, ?> model) {
        if (model.size() > 1) {
            return model;
        }
        return model.values().stream()
                .findFirst().get();
    }
}
