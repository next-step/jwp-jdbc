package core.mvc;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class HandlebarsView implements View {

    private static final String BASE_DIR = "webapp/";
    private static final String SUFFIX = ".html";

    private final Handlebars handlebars;
    private final String viewName;

    public HandlebarsView(String viewName) {
        this.handlebars = new Handlebars(new FileTemplateLoader(BASE_DIR, SUFFIX));
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Template template = handlebars.compile(viewName);
        template.apply(model, response.getWriter());
    }
}
