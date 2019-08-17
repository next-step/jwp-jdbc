package core.mvc;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {
    private static int MINIMUM_MODEL_SIZE = 1;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model.size() > MINIMUM_MODEL_SIZE) {
            JsonUtils.toJson(response.getOutputStream(), model);
        }

        for (String key : model.keySet()) {
            Object obj = model.get(key);
            JsonUtils.toJson(response.getOutputStream(), obj);
            return;
        }
    }
}
