package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonView implements View {
    private static final String DATA = "data";

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (model.isEmpty()) {
            RestRequestDispatcher.forward(request, response);
            return;
        }

        Set<String> keys = model.keySet();
        List<Object> models = new ArrayList<>();
        for (String key : keys) {
            models.add(model.get(key));
        }
        request.setAttribute(DATA, toJsonAsStringByModels(models));
        RestRequestDispatcher.forward(request, response);
    }

    private String toJsonAsStringByModels(List<Object> objects) {
        if (objects.size() == 1) {
            return JsonUtils.toJsonAsString(objects.get(0));
        }
        return JsonUtils.toJsonAsString(objects);
    }
}
