package core.mvc;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model.isEmpty()) {
            return;
        }

        final String body = JsonUtils.toString(getJsonViewModel(model));
        response.setContentLength(body.getBytes().length);
        response.getWriter().print(body);
    }

    private Object getJsonViewModel(Map<String, ?> model) {
        if (model.size() > 1) {
            return model;
        }

        return model.values().toArray()[0];
    }
}
