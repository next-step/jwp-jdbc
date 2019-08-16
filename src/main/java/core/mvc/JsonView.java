package core.mvc;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model == null || model.isEmpty()) {
            return;
        }

        String jsonString = JsonUtils.writeValue(getFirstIfSingle(model));
        response.getWriter().write(jsonString);
    }

    private Object getFirstIfSingle(Map<String, ?> model) {
        if (model.size() > 1) {
            return model;
        }
        return model.values().iterator().next();
    }
}
