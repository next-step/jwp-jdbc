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

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;

    private HttpStatus httpStatus;
    private Map<String, String> header;

    public JsonView() {
        this(HttpStatus.OK, Collections.emptyMap());
    }

    private JsonView(HttpStatus httpStatus, Map<String, String> header) {
        this.httpStatus = httpStatus;
        this.header = header;
    }

    public static JsonView ok() {
        return jsonView(HttpStatus.OK, Collections.emptyMap());
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
        setHeaders(response);

        if (CollectionUtils.isEmpty(model)) {
            return;
        }

        writeBody(response, JsonResponseConverter.convert(model));
    }

    private void setHeaders(HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);
        response.setStatus(httpStatus.value());
        header.forEach(response::setHeader);
    }

    private void writeBody(HttpServletResponse response, String jsonString) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println(jsonString);
        writer.flush();
    }

}
