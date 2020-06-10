package core.mvc.tobe.mock;

import core.mvc.View;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class MockView implements View {

    private String viewName;

    public MockView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (Map.Entry<String, ?> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());

        }

        if (response instanceof MockHttpServletResponse) {
            MockHttpServletResponse mockHttpServletResponse = (MockHttpServletResponse) response;
            final PrintWriter writer = mockHttpServletResponse.getWriter();
            writer.println(viewName);
            writer.flush();
        }
    }
}
