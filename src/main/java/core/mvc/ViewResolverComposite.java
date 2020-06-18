package core.mvc;

import java.util.Set;

public class ViewResolverComposite implements ViewResolver {

    private Set<ViewResolver> viewResolvers;

    public ViewResolverComposite(Set<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    @Override
    public View resolveViewName(String viewName) {
        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(viewName);

            if (view != null) {
                return view;
            }
        }

        return null;
    }
}
