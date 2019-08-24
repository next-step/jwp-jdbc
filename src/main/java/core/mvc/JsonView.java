package core.mvc;

import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE);
        if (CollectionUtils.isEmpty(model)) {
            return;
        }

        JsonResponseWriter.write(response.getWriter(), model);
    }
}
