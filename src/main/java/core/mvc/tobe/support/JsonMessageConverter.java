package core.mvc.tobe.support;

import core.mvc.JsonUtils;
import org.springframework.http.MediaType;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;

public class JsonMessageConverter implements MessageConverter {

    @Override
    public boolean canRead(HttpServletRequest request) {
        return MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType());
    }

    public <T> T read(String body, Class<T> targetType) {
        return JsonUtils.toObject(body, targetType);
    }


    @Override
    public void write(OutputStream outputStream, Object object) throws IOException {
        String body = JsonUtils.toJsonString(object);
        outputStream.write(body.getBytes());
        outputStream.flush();
    }
}
