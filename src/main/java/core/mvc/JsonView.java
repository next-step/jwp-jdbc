package core.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonView implements View {

    private static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);

        if (!model.isEmpty()) {
            final String jsonString = parseJsonString(model);
            write(jsonString, response);
        }
    }

    private static String parseJsonString(final Map<String, ?> model) {
        if (model.size() == 1) {
            final String key = String.valueOf(model.keySet().toArray()[0]);
            return JsonUtils.toJson(model.get(key));
        }
        return JsonUtils.toJson(model);
    }

    private void write(final String jsonString, final HttpServletResponse response) {
        try (final PrintWriter writer = response.getWriter()) {
            writer.write(jsonString);
            writer.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
