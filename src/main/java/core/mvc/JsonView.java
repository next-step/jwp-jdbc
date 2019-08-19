package core.mvc;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final String UTF_8 = "UTF-8";

    /*
    Map에 담긴 model 데이터가 1개인 경우 value 값을 반환, 2개 이상인 경우 Map 자체를 JSON으로 변환해 반환한다.
     */
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(UTF_8);

        final PrintWriter writer = response.getWriter();

        if (model == null || model.size() == 0) {
            writer.flush();
            return;
        }

        if (model.size() == 1) {
            model.forEach((key, value) -> writer.print(JsonUtils.toString(value)));
        } else {
            writer.print(JsonUtils.toString(model));
        }

        writer.flush();
    }
}
