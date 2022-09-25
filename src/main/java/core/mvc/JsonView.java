package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonView implements View {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        if (CollectionUtils.isEmpty(model)) {
            return;
        }

        String content = extractContent(model);

        PrintWriter writer = response.getWriter();
        writer.write(content);
    }

    private String extractContent(Map<String, ?> model) throws JsonProcessingException {
        List<Map.Entry<String, ?>> entries = new ArrayList<>(model.entrySet());

        if (entries.size() == 1) {
            Object value = entries.get(0).getValue();
            return objectMapper.writeValueAsString(value);
        }

        return objectMapper.writeValueAsString(model);
    }
}