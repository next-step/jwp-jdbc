package core.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private String viewName;

    public RedirectView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
    }

    @Override
    public String getViewName() {
        return viewName;
    }
}
