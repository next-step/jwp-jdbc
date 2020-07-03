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
        if (model.size() == 0) {
            RestRequestDispatcher.forward(request, response);
            return;
        }

        Set<String> keys = model.keySet();
        request.setAttribute(DATA, model);
        if (keys.size() > 1) {
            List<Object> models = new ArrayList<>();
            for (String key : keys) {
                models.add(model.get(key));
            }
            request.setAttribute(DATA, JsonUtils.toJsonAsString(models));
        } else {
            String firstKey = keys.stream()
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);

            request.setAttribute(DATA, JsonUtils.toJsonAsString(model.get(firstKey)));
        }

        RestRequestDispatcher.forward(request, response);

    }
}
