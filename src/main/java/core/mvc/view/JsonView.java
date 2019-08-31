package core.mvc.view;

import core.mvc.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

public class JsonView implements View {

    private static final String UTF_8 = "UTF-8";

    private final HttpStatus httpStatus;
    private final URI uri;

    public JsonView() {
        this(HttpStatus.OK, "/");
    }

    public JsonView(final String uri) {
        this(HttpStatus.OK, uri);
    }

    public JsonView(final HttpStatus httpStatus, final String uri) {
        this.httpStatus = httpStatus;
        this.uri = URI.create(uri);
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(UTF_8);
        setStatus(response);

        final PrintWriter writer = response.getWriter();
        final String json = parseJson(model);

        writer.print(json);
        writer.flush();
    }

    private void setStatus(final HttpServletResponse response) {
        response.setStatus(httpStatus.value());

        if (HttpStatus.CREATED == httpStatus) {
            response.addHeader("Location", uri.toString());
        }
    }

    private String parseJson(final Map<String, ?> model) {
        if (model == null || model.size() == 0) {
            return "";
        }

        if (model.size() == 1) {
            final ArrayList<?> objects = new ArrayList<>(model.values());
            return JsonUtils.toString(objects.get(0));
        }

        return JsonUtils.toString(model);
    }
}
