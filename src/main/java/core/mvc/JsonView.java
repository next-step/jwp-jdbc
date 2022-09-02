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
        String content = extractContent(model);
        PrintWriter writer = response.getWriter();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentLength(content.length());
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private String extractContent(Map<String, ?> model) {
        if (model.isEmpty()) {
            return "";
        }
        if (model.size() == 1) {
            return objectToString(model.values().iterator().next());
        }
        return objectToString(model);
    }

    private String objectToString(Object model) {
        try {
            return new ObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(String.format("%s를 JSON 형태로 변경할 수 없습니다.", model));
        }
    }
}
