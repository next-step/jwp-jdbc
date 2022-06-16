package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

public class JsonView implements View {
    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Object jsonObject = toJson(model);
        if(Objects.nonNull(jsonObject)) {
            objectMapper.writeValue(response.getOutputStream(), jsonObject);
        }
    }

    private Object toJson(Map<String, ?> model) {
        if (model.isEmpty()) {
            return null;
        }

        if (model.size() == 1) {
            return model.values().stream().findFirst().get();
        }

        return model;
    }
}
