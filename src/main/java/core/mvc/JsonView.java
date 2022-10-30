package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = convertObjectToJson(model);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if(!Strings.isNullOrEmpty(result)) {
            response.getWriter().write(result);
        }
    }

    private static String convertObjectToJson(Map<String, ?> model) throws JsonProcessingException {
        if(model.isEmpty()) {
            return null;
        }

        if(isSingleModel(model)) {
            String firstKey = getFirstKey(model);
            return objectMapper.writeValueAsString(model.get(firstKey));
        }

        return objectMapper.writeValueAsString(model);
    }

    private static String getFirstKey(Map<String, ?> model) {
        return model.keySet().stream()
                .findFirst()
                .get();
    }

    private static boolean isSingleModel(Map<String, ?> model) {
        return model.size() == 1;
    }
}
