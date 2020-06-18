package core.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.View;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = mapToJsonString(model);

        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.getWriter().write(json);
    }

    public String mapToJsonString(Map<String, ?> map) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (map.keySet().size() == 0) {
            return "";
        }

        if (map.keySet().size() == 1) {
            Object value = map.values().iterator().next();
            return objectMapper.writeValueAsString(value);
        }

        return objectMapper.writeValueAsString(map);
    }
}
