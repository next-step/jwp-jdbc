package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.OK.value());

        if (model == null || model.isEmpty()) {
            return;
        }

        String jsonString = JsonUtils.toString(getFirstIfSingle(model));
        response.getWriter().write(jsonString);
    }

    private Object getFirstIfSingle(Map<String, ?> model) {

        if (model.size() > 1) {
            return model;
        }

        return model.values().iterator().next();
    }
}
