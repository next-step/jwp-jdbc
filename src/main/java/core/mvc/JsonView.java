package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model,
                       HttpServletRequest request,
                       HttpServletResponse response) throws Exception {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        sendJson(model, response);
    }

    public void sendJson(Map<String, ?> model,
                                HttpServletResponse response) throws Exception{

        ObjectMapper objectMapper = new ObjectMapper();

        if(model.size() == 0){
            sendEmptyValueJson(response);
            return;
        }

        if(model.size() == 1){
            sendFirstValueJson(model, response, objectMapper);
            return;
        }

        sendValueJson(model, response, objectMapper);
    }

    private void sendEmptyValueJson(HttpServletResponse response) throws Exception{
        PrintWriter printWriter = response.getWriter();
        printWriter.print("");
        printWriter.flush();
    }

    private void sendFirstValueJson(Map<String, ?> model,
                                             HttpServletResponse response,
                                             ObjectMapper objectMapper) throws Exception{

        Object value = model.values().stream()
                .findFirst().orElseThrow(() -> new InvalidParameterException("값이 비어 있음"));

        objectMapper.writeValue(response.getOutputStream(), value);
    }

    private void sendValueJson(Map<String, ?> model,
                                        HttpServletResponse response,
                                        ObjectMapper objectMapper) throws Exception{

        objectMapper.writeValue(response.getOutputStream(), model);
    }
}
