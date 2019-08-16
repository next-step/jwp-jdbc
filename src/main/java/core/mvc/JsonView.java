package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static core.mvc.FunctionalExceptionWrapper.wrap;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(modelToString(model));
        response.getWriter().flush();
    }

    private String modelToString(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() > 1) {
            return JsonUtils.toJsonString(model);
        }

        return model.values().stream()
                .findAny()
                .map(wrap(JsonUtils::toJsonString))
                .orElse("");
    }
}
