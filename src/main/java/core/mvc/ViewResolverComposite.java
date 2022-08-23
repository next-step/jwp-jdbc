package core.mvc;

import java.util.ArrayList;
import java.util.List;

public class ViewResolverComposite {

    private List<ViewResolver> viewResolvers = new ArrayList<>();

    public void addViewResolver(ViewResolver viewResolver) {
        this.viewResolvers.add(viewResolver);
    }

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
