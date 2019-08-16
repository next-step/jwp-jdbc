package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonView implements View {
    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);

    public JsonView() {
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String jsonStr = "";
        ObjectMapper objectMapper = new ObjectMapper();

        if(model.size() != 0){
            jsonStr = objectMapper.writeValueAsString(getMapToFirstItem(model));
        }

        if(model.size() > 1){
            jsonStr = objectMapper.writeValueAsString(model.values().stream()
                    .map(val -> {
                        return getObjectToJson(objectMapper, val);
                    }).collect(Collectors.toList()));
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter printWriter = response.getWriter();

        printWriter.print(jsonStr);
        printWriter.flush();
    }

    public Object getMapToFirstItem(Map<String, ?> model){
        try {
            return model.values().stream()
                    .findFirst().orElseThrow(() -> new Exception("값이 비어 있음"));
        }catch (Exception e){
            logger.error("getMapToFirstItem Error {}", e.getMessage());
        }
        return "";
    }

    public Object getObjectToJson(ObjectMapper objectMapper, Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("getObjectToJson Error {}", e.getMessage());
        }
        return "";
    }
}
