package core.mvc;

import core.mvc.view.RedirectView;

public class RedirectViewResolver implements ViewResolver {

    public static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }

        return null;
    }
}
