package core.mvc;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = convertObjectToJson(model);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if(result != null) {
            response.getWriter().write(result);
        }
    }

    private static String convertObjectToJson(Map<String, ?> model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String result = null;
        if(isSingleModel(model)) {
            Iterator<String> keys = model.keySet().iterator();
            result = mapper.writeValueAsString(model.get(keys.next()));
        } else if(isMultipleModels(model)){
            result = mapper.writeValueAsString(model);
        }

        return result;
    }

    private static boolean isMultipleModels(Map<String, ?> model) {
        return model.size() > 1;
    }

    private static boolean isSingleModel(Map<String, ?> model) {
        return model.size() == 1;
    }
}
