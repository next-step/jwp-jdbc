package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {
    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = StringUtils.EMPTY;

        if (model.size() == 1) {
            String key = model.keySet().iterator().next();
            Object value = model.get(key);

            json = objectMapper.writeValueAsString(value);
        } else if (model.size() > 1) {
            json = objectMapper.writeValueAsString(model);
        }

        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
