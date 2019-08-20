package core.mvc;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

public class JsonView implements View {

    private static final int SINGLE_VALUE = 1;

    private final HttpStatus httpStatus;
    private final Map<String, String> header;

    public JsonView() {
        this(HttpStatus.OK, Collections.EMPTY_MAP);
    }

    private JsonView(HttpStatus httpStatus, Map<String, String> header) {
        this.httpStatus = httpStatus;
        this.header = header;
    }

    public static JsonView ok() {
        return jsonView(HttpStatus.OK, Collections.EMPTY_MAP);
    }

    public static JsonView ok(Map<String, String> header) {
        return jsonView(HttpStatus.OK, header);
    }

    public static JsonView created(Map<String, String> header) {
        return jsonView(HttpStatus.CREATED, header);
    }

    public static JsonView jsonView(HttpStatus httpStatus, Map<String, String> header) {
        return new JsonView(httpStatus, header);
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(httpStatus.value());
        header.forEach(response::addHeader);

        if (CollectionUtils.isEmpty(model)) {
            return;
        }

        String jsonString = convertJsonString(model);
        writeBody(response, jsonString);
    }

    private String convertJsonString(Map<String, ?> model) {
        if (model.size() == SINGLE_VALUE) {
            return JsonUtils.convertToJsonString(model.values().iterator().next());
        }

        return JsonUtils.convertToJsonString(model);
    }

    private void writeBody(HttpServletResponse response, String jsonString) throws IOException {
//        response.setContentLength(jsonString.length());

        PrintWriter writer = response.getWriter();
        writer.println(jsonString);
        writer.flush();
    }

}
