package core.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonView implements View {

    private HttpServletResponse response;
    private Map<String, ?> model;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws
        Exception {

        initialize(response, model);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        writeBody();
    }

    private void initialize(HttpServletResponse response, Map<String, ?> model) {
        this.response = response;
        this.model = model;
    }

    private void writeBody() throws IOException {
        String body = toJson();

        response.getWriter().write(body);
        response.getWriter().flush();
    }

    private String toJson() throws JsonProcessingException {
        if (model == null || model.size() == 0) {
            return "";
        }
        return JsonUtils.toJson(model);
    }
}
