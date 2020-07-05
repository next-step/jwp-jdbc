package core.mvc;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final int BOUNDARY_SIZE_OF_MODEL = 2;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        PrintWriter writer = response.getWriter();
        if (hasSeveral(model)) {
            writer.write(JsonUtils.writeValueAsString(model));
            return;
        }

        for (Object value : model.values()) {
            writer.write(JsonUtils.writeValueAsString(value));
        }
    }

    private boolean hasSeveral(Map<String, ?> model) {
        return model.size() > BOUNDARY_SIZE_OF_MODEL;
    }
}
