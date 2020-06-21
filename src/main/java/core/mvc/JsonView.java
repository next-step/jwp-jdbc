package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (model.isEmpty()) {
            return;
        }

        ServletOutputStream outputStream = response.getOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        if (model.size() == 1) {
            Object value = new ArrayList<>(model.values()).get(0);
            outputStream.write(objectMapper.writeValueAsBytes(value));
            return;
        }

        outputStream.write(objectMapper.writeValueAsBytes(model));
    }
}
