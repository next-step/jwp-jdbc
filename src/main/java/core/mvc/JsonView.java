package core.mvc;

import next.support.ObjectMapperFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (model.size() == 1) {
            ObjectMapperFactory.getInstance().writeValue(response.getOutputStream(), this.getSingleData(model));
            return;
        }

        ObjectMapperFactory.getInstance().writeValue(response.getOutputStream(), model);
    }

    private Object getSingleData(Map<String, ?> model) {
        return model.values().stream()
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
