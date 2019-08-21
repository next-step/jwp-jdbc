package core.mvc.view;

import core.mvc.JsonUtils;
import org.springframework.http.MediaType;
import support.exception.FunctionWithException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        PrintWriter writer = response.getWriter();
        String body = model.values()
                .stream()
                .map(FunctionWithException.wrapper(JsonUtils::toJson))
                .collect(Collectors.joining(","));

        writer.print(body);
        writer.flush();
    }

    @Override
    public String getViewName() {
        return null;
    }
}
