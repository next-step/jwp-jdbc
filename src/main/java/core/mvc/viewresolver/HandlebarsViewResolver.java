package core.mvc.viewresolver;

import core.mvc.HandlebarsView;
import core.mvc.View;

public class HandlebarsViewResolver extends TemplateViewResolver {

    private static final String SUFFIX = ".html";

    protected HandlebarsViewResolver() {
        super(SUFFIX);
    }

    @Override
    public View resolve(String viewName) {
        return new HandlebarsView(viewName);
    }
}
