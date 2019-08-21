package core.mvc;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if(model.size() == 0) {
            return;
        }
        final PrintWriter writer = response.getWriter();
        if(model.size() == 1) {
            final Map.Entry<String, ?> next = model.entrySet().iterator().next();
            final Object value = next.getValue();
            writer.print(JsonUtils.toString(value));
        }
        writer.print(JsonUtils.toString(model));
    }
}
