package core.mvc;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String content = parseContent(model);
        PrintWriter writer = response.getWriter();
        writer.write(content);
        writer.close();
    }

    private String parseContent(Map<String, ?> model) {
        Set<String> keys = model.keySet();
        if (keys.size() <= 1) {
            return keys.stream()
                    .map(model::get)
                    .map(JsonUtils::toString)
                    .findFirst()
                    .orElse("");
        }

        return JsonUtils.toString(model);
    }
}
