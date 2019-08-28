package core.mvc.view;

public class JsonViewResolver implements ViewResolver {
    private static final String VIEW_NAME = "JsonView";

    @Override
    public boolean supports(String viewName) {
        return VIEW_NAME.equalsIgnoreCase(viewName);
    }

    @Override
    public View resolve(String viewName) {
        return new JsonView();
    }
}
