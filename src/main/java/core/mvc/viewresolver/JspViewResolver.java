package core.mvc.viewresolver;

import core.mvc.JspView;
import core.mvc.View;

public class JspViewResolver extends TemplateViewResolver {

    private static final String JSP_EXTENSION = ".jsp";

    protected JspViewResolver() {
        super(JSP_EXTENSION);
    }

    @Override
    public View resolve(String viewName) {
        return new JspView(viewName);
    }
}
