package core.mvc;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String json = getJsonFromModel(model);
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    private String getJsonFromModel(Map<String, ?> model) {
        if (model.size() == 1) {
            return JsonUtils.toJson(model.values().iterator().next());
        }
        return model.isEmpty() ? "" : JsonUtils.toJson(model);
    }
}
