package core.mvc;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_VALUE);
        Set<String> keys = model.keySet();

        if (keys.size() > 1) {
            response.getWriter().println(JsonUtils.toJsonAsString(model));
            return;
        }
        for (String key : keys) {
            response.getWriter().println(JsonUtils.toJsonAsString(model.get(key)));
        }
    }
}
