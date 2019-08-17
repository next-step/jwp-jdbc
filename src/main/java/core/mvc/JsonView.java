package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

public class JsonView implements View {
    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("JsonView");
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

        Set<String> keys = model.keySet();
        if (keys.size() <= 1) {
            for (Object value : model.values()) {
                String serializedValue = JsonUtils.toJsonString(value);
                response.getWriter().write(serializedValue);
            }
        } else {
            String serializedModel = JsonUtils.toJsonString(model);
            response.getWriter().write(serializedModel);
        }

        response.getWriter().flush();
        response.getWriter().close();
    }
}
