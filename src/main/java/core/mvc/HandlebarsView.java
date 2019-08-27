package core.mvc;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class HandlebarsView implements View {

    private static final String BASE_DIR = "webapp";
    private static final String SUFFIX = ".html";

    private final String viewName;
    private final Handlebars handlebars;

    public HandlebarsView(String viewName) {
        if (viewName.endsWith(SUFFIX)) {
            viewName = removeSuffix(viewName);
        }

        this.viewName = viewName;
        this.handlebars = new Handlebars(new FileTemplateLoader(BASE_DIR, SUFFIX));
    }

    private String removeSuffix(String viewName) {
        return viewName.substring(0, viewName.lastIndexOf(SUFFIX));
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Template template = handlebars.compile(viewName);
        template.apply(model, response.getWriter());
    }
}
