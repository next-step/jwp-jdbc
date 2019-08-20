package core.mvc;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

public class JsonView implements View {

  @Override
  public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.getWriter().write(getValue(model));
  }

  private String getValue(Map<String, ?> model) {
    if (model.size() > 1) {
      return JsonUtils.toJson(model);
    }
    return model.values().stream()
        .map(JsonUtils::toJson)
        .findFirst()
        .orElse(StringUtils.EMPTY);
  }
}
