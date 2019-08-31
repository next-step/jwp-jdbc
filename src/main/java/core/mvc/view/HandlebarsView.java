package core.mvc.view;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import next.WebServerLauncher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

public class HandlebarsView implements View {

    private final Handlebars handlebars;
    private final String viewName;

    public HandlebarsView(String viewName) {
        if (viewName == null) {
            throw new NullPointerException("viewName is null. 이동할 URL을 입력하세요.");
        }

        this.handlebars = createHandlebars();
        this.viewName = viewName;
    }

    private Handlebars createHandlebars() {
        final File file = new File(WebServerLauncher.WEBAPP_DIR_LOCATION);
        return new Handlebars(new FileTemplateLoader(file));
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Template template = handlebars.compile(viewName);
        final String result = template.apply(model);

        final PrintWriter writer = response.getWriter();
        writer.print(result);
        writer.flush();
    }
}
