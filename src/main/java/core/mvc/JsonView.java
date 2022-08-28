package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {
    private static final int ONE_MODEL_COUNT = 1;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String value = getValue(model, new ObjectMapper());
        if (value != null) {
            response.getWriter().write(value);
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private String getValue(Map<String, ?> model, ObjectMapper objectMapper) throws JsonProcessingException {
        if (model.size() < ONE_MODEL_COUNT) {
            return null;
        }

        if (model.size() == ONE_MODEL_COUNT) {
            for (String key : model.keySet()) {
                Object modelValue = model.get(key);
                return objectMapper.writeValueAsString(modelValue);
            }
        }

        return objectMapper.writeValueAsString(model);
    }
}
