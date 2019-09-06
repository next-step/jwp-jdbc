package core.mvc.view;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ServletContextTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import core.mvc.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class HandlebarsView implements View {
    private static final Logger logger = LoggerFactory.getLogger(HandlebarsView.class);
    private String viewName;

    public HandlebarsView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        TemplateLoader templateLoader = new ServletContextTemplateLoader(request.getServletContext());
        templateLoader.setPrefix("");
        templateLoader.setSuffix("");

        Handlebars handlebars = new Handlebars(templateLoader);
        Template template = handlebars.compile(viewName);

        byte[] body = template.apply(model).getBytes();
        response.getOutputStream().write(body);
        response.getOutputStream().flush();
    }
}
