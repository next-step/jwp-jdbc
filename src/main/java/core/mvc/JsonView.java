package core.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.http.MediaType;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = toJsonString(model);

        setContentType(response);
        setContent(response, data);
    }

    private void setContentType(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private void setContent(HttpServletResponse response, String data) throws IOException {
         PrintWriter writer = response.getWriter();

         writer.write(data);
         writer.flush();
         writer.close();
    }

    private String toJsonString(Map<String, ?> model) {
        if (model.isEmpty()) {
            return "";
        }

        if (model.size() == 1) {
            Object data = model.values().iterator().next();
            return JsonUtils.objectToJson(data);
        }

        return JsonUtils.objectToJson(model);
    }
}
