package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class JsonView implements View {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (model.isEmpty()) {
            return;
        }

        ServletOutputStream outputStream = response.getOutputStream();
        if (model.size() == 1) {
            Object value = new ArrayList<>(model.values()).get(0);
            write(outputStream, value);
            return;
        }

        write(outputStream, model);
    }

    private void write(ServletOutputStream outputStream, Object value) throws IOException {
        outputStream.write(OBJECT_MAPPER.writeValueAsBytes(value));
        outputStream.flush();
    }
}
