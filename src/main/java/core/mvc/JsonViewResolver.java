package core.mvc;

import core.mvc.view.JsonView;

/**
 * @author KingCjy
 */
public class JsonViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        if (viewName == null) {
            return new JsonView();
        }
        return null;
    }
}
