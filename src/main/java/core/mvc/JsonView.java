package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonView implements View {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String UTF_8_ENCODING = "UTF-8";

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Object bodyValue = getBodyValue(model);
        response.setContentLength(getContentLength(bodyValue));
        response.setCharacterEncoding(UTF_8_ENCODING);

        if(!model.isEmpty()){
            response.getWriter().write(serialize(bodyValue));
        }
    }

    private String serialize(Object bodyValue) throws JsonProcessingException, UnsupportedEncodingException {
        return OBJECT_MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(bodyValue);
    }

    private int getContentLength(Object bodyValue) {
        return String.valueOf(bodyValue).getBytes().length;
    }

    private Object getBodyValue(Map<String, ?> model) {
        if(model.keySet().size() == 0){
            return "";
        }

        if (model.keySet().size() == 1) {
            return model.keySet().stream()
                    .map(key -> model.get(key))
                    .findFirst()
                    .get();
        }

        return model;
    }
}
