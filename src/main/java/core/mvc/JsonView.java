package core.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws
        Exception {

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        writeBody(model, response);
    }

    private void writeBody(Map<String, ?> model, HttpServletResponse response) throws IOException {
        String body = toJson(model);

        response.getWriter().write(body);
        response.getWriter().flush();
    }

    private String toJson(Map<String, ?> model) throws JsonProcessingException {
        if (model == null || model.size() == 0) {
            return "";
        }
        return JsonUtils.toJson(model);
    }
}
