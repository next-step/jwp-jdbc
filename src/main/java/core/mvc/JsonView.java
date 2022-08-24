package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {
    private static final int ONE_MODEL_COUNT = 1;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String value = null;
        if (model.size() == ONE_MODEL_COUNT) {
            for (String key : model.keySet()) {
                Object modelValue = model.get(key);
                value = objectMapper.writeValueAsString(modelValue);
            }
        }

        if (value != null) {
            response.getWriter().write(value);
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
