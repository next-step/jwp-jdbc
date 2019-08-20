package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int SIZE_OF_EXTRACT = 1;

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (model.isEmpty()) {
            return;
        }

        response.getWriter()
                .write(objectMapper.writeValueAsString(extract(model)));
    }

    private Object extract(final Map<String, ?> model) {
        if (model.size() != SIZE_OF_EXTRACT) {
            return model;
        }

        return model.entrySet()
                .stream()
                .findFirst()
                .map(Map.Entry::getKey)
                .map(model::get)
                .get();
    }
}
