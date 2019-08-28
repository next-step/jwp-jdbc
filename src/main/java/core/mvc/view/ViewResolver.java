package core.mvc.view;

public interface ViewResolver {
    boolean supports(String viewName);

    View resolve(String viewName);
}
