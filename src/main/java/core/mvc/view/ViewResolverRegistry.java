package core.mvc.view;

import core.mvc.ModelAndView;
import core.mvc.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ViewResolverRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ViewResolverRegistry.class);
    private List<ViewResolver> viewResolvers = new ArrayList<>();

    public void addViewResolver(ViewResolver viewResolver) {
        viewResolvers.add(viewResolver);
    }

    public View getView(ModelAndView modelAndView) {
        if(modelAndView.getView() != null) {
            return modelAndView.getView();
        }
        logger.debug("viewName : {} ",modelAndView.getViewName());
        ViewResolver resolver = viewResolvers.stream()
                .filter(viewResolver -> viewResolver.canHandle(modelAndView))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("can not find viewResolver"));

        return resolver.resolve(modelAndView);
    }
}
