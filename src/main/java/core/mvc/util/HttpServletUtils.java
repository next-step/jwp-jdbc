package core.mvc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class HttpServletUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpServletUtils.class);

    public static Object jsonBodyToObject(ObjectMapper om, HttpServletRequest request, Class<?> clazz) {
        String jsonString = extractRequestBody(request);
        return JsonUtils.toObject(om, jsonString, clazz);
    }

    private static String extractRequestBody(HttpServletRequest request) {
        String requestBody = "";
        try (BufferedReader br = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            sb.append(br.readLine());
            requestBody = sb.toString();
            logger.debug("requestBody : {}", requestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBody;
    }
}
