package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonView implements View {
    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {

        try (PrintWriter writer = response.getWriter()) {

            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

            String content = extractContent(model);
            writer.write(content);

        } catch (IOException ie) {
            logger.error(ie.getMessage(), ie);
        }
    }

    private String extractContent(Map<String, ?> model) throws JsonProcessingException {
        List<Entry<String, ?>> entries = new ArrayList<>(model.entrySet());

        if (entries.size() == 1) {
            Object value = entries.get(0).getValue();
            return objectMapper.writeValueAsString(value);
        }

        return objectMapper.writeValueAsString(model);
    }
}
