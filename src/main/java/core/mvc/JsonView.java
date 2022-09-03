package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.Set;

public class JsonView implements View {

    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String EMPTY_STRING = "";

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String content = content(model);
        PrintWriter writer = response.getWriter();

        Set<String> keys = model.keySet();
        for (String key : keys) {
            logger.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentLength(content.length());

        writer.write(content);
        writer.flush();
        writer.close();

    }

    private String content(Map<String, ?> model) {
        if (CollectionUtils.isEmpty(model)) {
            return EMPTY_STRING;
        }

        if (model.size() == 1) {
            return objectToString(model.values().toArray()[0]);
        }

        return objectToString(model);
    }

    private String objectToString(Object model) {
        try {
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }
}
