package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final String json = mapToJson(model);
        final PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }

    private String mapToJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return JsonUtils.stringify(model.values().iterator().next());
        }
        return !model.isEmpty() ? JsonUtils.stringify(model) : "";
    }
}
