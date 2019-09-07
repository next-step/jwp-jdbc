package core.mvc.view;

import core.mvc.ModelAndView;
import core.mvc.View;

public interface ViewResolver {
    boolean canHandle(ModelAndView modelAndView);

    View resolve(ModelAndView modelAndView);
}
