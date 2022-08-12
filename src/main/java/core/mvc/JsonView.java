package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (model.size() > 0) {
            response.getWriter().write(convertModel(model));
        }
    }

    private String convertModel(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() > 1) {
            return mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(model);
        }

        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(model.get(getFirstKey(model)));
    }

    private String getFirstKey(Map<String, ?> model) {
        return model.keySet()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("모델이 비었습니다."));
    }
}
