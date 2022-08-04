package core.mvc;

import core.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonView implements View {
    private static final String JSON_VIEW_RESPONSE_CONTENT_TYPE = "application/json";

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.setResponseContentType(response);

        if (model.isEmpty()) {
            return;
        }

        if (this.hasOneDataIn(model)) {
            this.responseOneDataInModel(model, response);
            return;
        }

        responseModel(model, response);
    }

    private void setResponseContentType(HttpServletResponse response) {
        response.setContentType(JSON_VIEW_RESPONSE_CONTENT_TYPE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
    }

    private boolean hasOneDataIn(Map<String, ?> model) {
        return model.size() == 1;
    }

    private void responseOneDataInModel(Map<String, ?> model, HttpServletResponse response) throws IOException {
        this.writeResponseBodyAsJson(response, CollectionUtils.getFirstElement(model.values()));
    }

    private void responseModel(Map<String, ?> model, HttpServletResponse response) throws IOException {
        this.writeResponseBodyAsJson(response, model);
    }

    private void writeResponseBodyAsJson(HttpServletResponse response, Object object) throws IOException {
        PrintWriter writer = response.getWriter();
        String json = JsonUtils.toJson(object);
        writer.print(json);
        writer.flush();
    }
}
