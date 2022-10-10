package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String content = extractContent(model);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private String extractContent(Map<String, ?> model) {
        if (CollectionUtils.isEmpty(model)) {
            return "";
        }

        if (model.size() == 1) {
            return objectToString(model.values().stream()
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new));
        }

        return objectToString(model);
    }

    private String objectToString(Object model) {
        try {
            return new ObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Cannot convert %s to Json type.", model));
        }
    }
}
