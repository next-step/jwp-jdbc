package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ServletOutputStream outputStream = response.getOutputStream();

        if (model.size() > 1) {
            String body = toJson(model);
            write(outputStream, body);
            return;
        }

        Optional<String> key = model.keySet().stream().findFirst();
        String body = key.map(e -> toJson(model.get(e))).orElseGet(() -> "");
        write(outputStream, body);
    }

    private void write(ServletOutputStream outputStream, String body) throws IOException {
        outputStream.write(body.getBytes());
        outputStream.flush();
    }

    private String toJson(Object object) throws ObjectMapperException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ObjectMapperException(e);
        }
    }
}
