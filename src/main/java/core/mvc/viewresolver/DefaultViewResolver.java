package core.mvc.viewresolver;

import core.mvc.JspView;
import core.mvc.View;

import java.util.function.Function;

public class DefaultViewResolver implements ViewResolver {

    private static final Function<String, View> DEFAULT_VIEW_FUNCTION = JspView::new;

    @Override
    public boolean supports(String viewName) {
        return true;
    }

    @Override
    public View resolve(String viewName) {
        return DEFAULT_VIEW_FUNCTION.apply(viewName);
    }

}
