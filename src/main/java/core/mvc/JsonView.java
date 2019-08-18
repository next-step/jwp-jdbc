package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int MIN_MODEL_SIZE = 1;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = parseToJson(model);
        writeJson(response, json);
    }

    private void writeJson(HttpServletResponse response, String json) throws IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        writer.print(json);
        writer.flush();
    }

    private String parseToJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() < MIN_MODEL_SIZE) {
            return StringUtils.EMPTY;
        }

        if (model.size() > MIN_MODEL_SIZE) {
            return MAPPER.writeValueAsString(model);
        }
        return MAPPER.writeValueAsString(model.values().toArray()[0]);
    }
}
