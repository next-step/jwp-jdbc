package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {
    ObjectMapper om = new ObjectMapper();

    private static final String EMTPY_STRING = "";

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter writer = response.getWriter();
        writer.write(mapToString(model));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private String mapToString(Map<String, ?> model) throws JsonProcessingException {
        if (CollectionUtils.isEmpty(model)) {
            return EMTPY_STRING;
        }

        if (model.size() == 1) {
            return om.writeValueAsString(model.values().toArray()[0]);
        }

        return om.writeValueAsString(model);
    }
}
