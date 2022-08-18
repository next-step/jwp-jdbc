package core.mvc;

import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

public class JsonView implements View {

    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model == null || model.isEmpty()) {
            return;
        }

        if (model.size() == 1) {
            for (Entry<String, ?> entry : model.entrySet()) {
                response.getWriter().write(JsonUtils.fromObject(entry.getValue()));
            }
            return;
        }

        response.getWriter().write(JsonUtils.fromObject(model));
    }
}
