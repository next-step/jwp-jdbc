package core.mvc.view;

import core.mvc.ModelAndView;
import core.mvc.View;

public class RedirectViewResolver implements ViewResolver {
    @Override
    public boolean canHandle(ModelAndView modelAndView) {
        return modelAndView.getViewName().startsWith("redirect:");
    }

    @Override
    public View resolve(ModelAndView modelAndView) {
        return new JspView(modelAndView.getViewName());
    }
}
