package next.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.javafx.jmx.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil {

    public static Map<String, Object> getParameter(HttpServletRequest request) throws IOException {
        Map<String, Object> map = new HashMap<>();

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        try {
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(jb.toString(), new TypeReference<Map<String, String>>(){});
        } catch (JSONException e) {
            // crash and burn
            throw new IOException("Error parsing JSON request string");
        }

        return map;
    }

}
