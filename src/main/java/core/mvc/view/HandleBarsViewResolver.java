package core.mvc.view;

import core.mvc.ModelAndView;
import core.mvc.View;

public class HandleBarsViewResolver implements ViewResolver {

    @Override
    public boolean canHandle(ModelAndView modelAndView) {
        return modelAndView.getViewName().endsWith(".hbs");
    }

    @Override
    public View resolve(ModelAndView modelAndView) {
        return new HandlebarsView(modelAndView.getViewName());
    }
}
