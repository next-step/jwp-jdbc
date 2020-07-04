package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Object bodyValue = getBodyValue(model);
        response.setContentLength(getContentLength(bodyValue));
        response.getWriter().write(serialize(bodyValue));
    }

    private String serialize(Object bodyValue) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(bodyValue);
    }

    private int getContentLength(Object bodyValue) {
        return String.valueOf(bodyValue).getBytes().length;
    }

    private Object getBodyValue(Map<String, ?> model) {
        Object bodyValue = null;

        if (model.keySet().size() == 1) {
            bodyValue = model.keySet().stream()
                    .map(key -> model.get(key))
                    .findFirst()
                    .get();
        }

        return bodyValue;
    }
}
