package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {
    private final String contentType = MediaType.APPLICATION_JSON_VALUE;
    private ObjectMapper objectMapper = new ObjectMapper();
    private HttpStatus status = HttpStatus.OK;
    private String location;

    public JsonView() {
    }

    public JsonView(HttpStatus status, String location) {
        this.status = status;
        this.location = location;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");
        String jsonValue = StringUtils.EMPTY;

        if(!model.isEmpty()) {
            jsonValue = getJsonValue(model);
            System.out.println(jsonValue);
        }

        response.getWriter().write(jsonValue);
        response.setStatus(status.value());
        if(StringUtils.isNotEmpty(this.location)) {
            response.setHeader("Location", this. location);
        }
    }

    private String getJsonValue(Map<String, ?> model) throws JsonProcessingException {
        if(model.size() == 1) {
            return objectMapper.writeValueAsString(model.keySet().stream()
                    .map(model::get)
                    .findFirst()
                    .get());
        }

        return objectMapper.writeValueAsString(model);
    }
}
