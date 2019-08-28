package core.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import core.utils.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static support.exception.ExceptionFunction.wrap;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setStatus(response);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(modelToString(model));
        response.getWriter().flush();
    }

    private void setStatus(HttpServletResponse response) {
        String location = response.getHeader("location");
        if (!Strings.isNullOrEmpty(location)) {
            response.setStatus(HttpStatus.CREATED.value());
        }
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
