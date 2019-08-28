package core.mvc.view;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ViewResolverRegistry {
    private List<ViewResolver> viewResolvers;

    public ViewResolverRegistry() {
        viewResolvers = new ArrayList<>();
    }

    public void add(ViewResolver viewResolver) {
        viewResolvers.add(viewResolver);
    }

    public View getView(String viewName) throws FileNotFoundException {
        return viewResolvers.stream()
                .filter(viewResolver -> viewResolver.supports(viewName))
                .map(viewResolver -> viewResolver.resolve(viewName))
                .findFirst()
                .orElseThrow(FileNotFoundException::new);
    }
}
