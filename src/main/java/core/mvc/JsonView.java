package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonView implements View {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (model.isEmpty()) {
            return;
        }

        String body = writeValueAsString(model);
        response.setContentLength(body.getBytes(StandardCharsets.UTF_8).length);

        PrintWriter writer = response.getWriter();
        writer.write(body);
        writer.flush();
    }

    private String writeValueAsString(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            Object value = model.values()
                    .stream()
                    .findFirst()
                    .get();

            return OBJECT_MAPPER.writeValueAsString(value);
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
