package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        PrintWriter writer = response.getWriter();
        writer.write(generatorResponseBody(model));
        writer.flush();
    }

    private String generatorResponseBody(Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            return "";
        }

        if (model.size() == 1) {
            return objectToString(model.values().iterator().next());
        }

        return objectToString(model);
    }

    private String objectToString(Object model) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(model);
    }
}
