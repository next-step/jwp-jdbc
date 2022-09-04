package core.mvc;

import next.support.mapper.ObjectMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.Set;

public class JsonView implements View {

    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Set<String> keys = model.keySet();
        for (String key : keys) {
            logger.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        jsonResponse(response.getOutputStream(), model);

    }

    private void jsonResponse(ServletOutputStream outputStream, Map<String, ?> model) {
        if (CollectionUtils.isEmpty(model)) {
            return ;
        }

        if (model.size() == 1) {
            writeJsonValue(outputStream, model.values().toArray()[0]);
        }

        writeJsonValue(outputStream, model);
    }

    private void writeJsonValue(ServletOutputStream outputStream, Object model) {
        try {
            ObjectMapperFactory.getInstance().writeValue(outputStream, model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
