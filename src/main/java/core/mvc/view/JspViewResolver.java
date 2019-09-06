package core.mvc.view;

import core.mvc.ModelAndView;
import core.mvc.View;

public class JspViewResolver implements ViewResolver{
    @Override
    public boolean canHandle(ModelAndView modelAndView) {
        return modelAndView.getViewName().endsWith(".jsp");
    }
    @Override
    public View resolve(ModelAndView modelAndView) {
        return new JspView(modelAndView.getViewName());
    }
}
