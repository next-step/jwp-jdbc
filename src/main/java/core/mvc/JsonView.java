package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final PrintWriter writer = response.getWriter();
        writeJson(model, writer);
    }

    private void writeJson(Map<String, ?> model, PrintWriter writer) throws JsonProcessingException {
        if(model.size() == 0)
            return;

        Object target = pickValueToJson(model);
        writer.print(JsonUtils.toString(target));
    }

    private static Object pickValueToJson(Map<String, ?> model) {
        if(model.size() == 1) {
            final Map.Entry<String, ?> next = model.entrySet().iterator().next();
            return next.getValue();
        }
        return model;
    }
}