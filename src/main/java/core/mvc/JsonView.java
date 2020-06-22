package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private int statusCode;
    private String url;

    public JsonView(int statusCode) {
        this.statusCode = statusCode;
    }

    public JsonView(int statusCode, String url) {
        this.statusCode = statusCode;
        this.url = url;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String body = getBody(model);
        final int contentLength = body.getBytes().length;

        response.setStatus(statusCode);
        if (url != null) {
            response.setHeader("Location", url);
        }
        response.setContentType("application/json;charset=UTF-8");
        response.setContentLength(contentLength);
        response.getWriter().write(body);
    }

    private String getBody(Map<String, ?> model) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();

        if (model.isEmpty()) {
            return "";
        }

        if (model.size() == 1) {
            final Object o = model.keySet().stream()
                    .map(k -> model.get(k))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            return objectMapper.writeValueAsString(o);
        }

        return objectMapper.writeValueAsString(model);
    }
}
