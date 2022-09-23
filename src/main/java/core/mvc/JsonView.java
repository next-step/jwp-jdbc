package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final String DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final String BLANK = "";

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String content = extractContent(model);
        PrintWriter writer = response.getWriter();

        response.setContentType(DEFAULT_CONTENT_TYPE);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private String extractContent(Map<String, ?> model) {
        int size = model.size();
        if (size == 0) {
            return BLANK;
        }
        if (size == 1) {
            return objectToString(model.values().iterator().next());
        }
        return objectToString(model);
    }

    private String objectToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(String.format("fail to convert %s to string", object));
        }
    }
}
