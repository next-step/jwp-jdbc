package core.mvc.viewresolver;

import core.mvc.JsonView;
import core.mvc.View;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ViewNameViewResolver implements ViewResolver {

    private static final Map<String, Function<String, View>> SUPPORT_VIEW_NAMES = new HashMap<>();

    static {
        SUPPORT_VIEW_NAMES.put("jsonView", viewName -> new JsonView());
    }

    @Override
    public boolean supports(String viewName) {
        return SUPPORT_VIEW_NAMES.containsKey(viewName);
    }

    @Override
    public View resolve(String viewName) {
        return SUPPORT_VIEW_NAMES.get(viewName).apply(viewName);
    }
}
