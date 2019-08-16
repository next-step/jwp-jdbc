package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CreatedView implements View {

    private String location;

    public CreatedView(String location) {
        this.location = location;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader("Location", location);
    }
}
