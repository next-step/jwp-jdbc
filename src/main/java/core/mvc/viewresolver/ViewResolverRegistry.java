package core.mvc.viewresolver;

import core.mvc.View;

import java.util.Arrays;
import java.util.List;

public class ViewResolverRegistry {

    private static final ViewResolver DEFAULT_VIEW_RESOLVER = new DefaultViewResolver();

    private final List<ViewResolver> viewResolvers;

    public ViewResolverRegistry(ViewResolver... viewResolver) {
        this(Arrays.asList(viewResolver));
    }

    public ViewResolverRegistry(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public View resolveView(String viewName) {
        return viewResolvers.stream()
                .filter(viewResolver -> viewResolver.supports(viewName))
                .findFirst()
                .orElse(DEFAULT_VIEW_RESOLVER)
                .resolve(viewName);
    }

}
