package core.mvc;

import core.mvc.messageconverter.JsonMessageConverter;
import core.mvc.messageconverter.JsonObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {
    private static final int MINIMUM_MODEL_SIZE = 1;

    private JsonMessageConverter converter;

    public JsonView() {
        converter = new JsonMessageConverter(JsonObjectMapper.builder().build());
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ServletOutputStream outputStream = response.getOutputStream();

        if (model.size() > MINIMUM_MODEL_SIZE) {
            converter.writeMessage(outputStream, model);
            return;
        }

        for (String key : model.keySet()) {
            Object obj = model.get(key);
            converter.writeMessage(outputStream, obj);
            return;
        }
    }
}
