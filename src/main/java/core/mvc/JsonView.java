package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        if (model.isEmpty()) {
            return;
        }

        if (model.size() == 1) {
            writeModel(getFirstValue(model), response);
            return;
        }

        writeModel(model, response);
    }

    private Object getFirstValue(Map<String, ?> model) {
        return model.values()
                .stream()
                .findFirst()
                .stream()
                .findFirst()
                .orElseThrow();
    }

    private void writeModel(Object json, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.print(MAPPER.writeValueAsString(json));
        writer.flush();
    }
}
