package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Optional<?> maybeBody = getBody(model);
        if (maybeBody.isPresent()) {
            objectMapper.writeValue(response.getWriter(), maybeBody.get());
        }
    }

    private Optional<?> getBody(Map<String, ?> model) {
        if (model.isEmpty()) {
            return Optional.empty();
        }

        if (model.size() == 1) {
            return model.values()
                .stream()
                .findFirst();
        }

        return Optional.of(model.values());
    }
}
