package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (!model.isEmpty()) {
            Object object = getObject(model);

            PrintWriter writer = response.getWriter();
            writer.write(mapper.writeValueAsString(object));
            writer.close();
        }
    }

    private Object getObject(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().stream()
                    .findAny()
                    .get();
        }
        return model;
    }
}
