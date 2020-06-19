package core.mvc;

public interface ViewResolver {
    View resolveViewName(String viewName);
}
