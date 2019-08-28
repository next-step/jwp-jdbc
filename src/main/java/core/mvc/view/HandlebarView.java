package core.mvc.view;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class HandlebarView implements View {
    private String viewName;

    public HandlebarView(String viewName) {
        if (viewName == null) {
            throw new NullPointerException("viewName is null. 출력할 View File 을 입력하세요.");
        }
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FileTemplateLoader fileTemplateLoader = new FileTemplateLoader("webapp/");
        Handlebars handlebars = new Handlebars(fileTemplateLoader);
        Template template = handlebars.compile(viewName);

        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.getWriter().print(template.apply(model));
        response.getWriter().flush();
    }
}
