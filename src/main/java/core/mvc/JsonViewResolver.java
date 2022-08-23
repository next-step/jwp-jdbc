package core.mvc;

import static core.mvc.JsonView.JSON_VIEW_PREFIX;

public class JsonViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith(JSON_VIEW_PREFIX)) {
            return new JsonView(viewName.substring(viewName.indexOf(":") + 1));
        }
        return null;
    }
}
