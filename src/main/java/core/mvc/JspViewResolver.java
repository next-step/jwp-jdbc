package core.mvc;

import core.mvc.view.JspView;

public class JspViewResolver implements ViewResolver {

    public static final String JSP_SUFFIX = ".jsp";

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.endsWith(JSP_SUFFIX)) {
            return new JspView(viewName);
        }

        return null;
    }
}
