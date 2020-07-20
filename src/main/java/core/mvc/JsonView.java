package core.mvc;

import core.mvc.tobe.support.JsonMessageConverter;
import org.springframework.http.MediaType;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class JsonView implements View {

    private final JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ServletOutputStream outputStream = response.getOutputStream();

        if(model.size() == 0) {
            return;
        }

        if (model.size() > 1) {
            jsonMessageConverter.write(outputStream, model);
            return;
        }

        jsonMessageConverter.write(outputStream, model.values().iterator().next());
    }
}
