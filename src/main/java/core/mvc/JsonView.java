package core.mvc;

import com.google.common.net.HttpHeaders;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(modelToString(model));
        response.getWriter().flush();
    }

    private String modelToString(Map<String, ?> model) {
        if (model.size() > 1) {
            return JsonUtils.toJsonString(model);
        }
        return model.values()
                .stream()
                .findFirst()
                .map(JsonUtils::toJsonString)
                .orElse(StringUtils.EMPTY);
    }
}
