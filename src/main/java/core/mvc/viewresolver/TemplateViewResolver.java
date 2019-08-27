package core.mvc.viewresolver;

import java.util.Objects;

public abstract class TemplateViewResolver implements ViewResolver {

    private final String extension;

    protected TemplateViewResolver(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean supports(String viewName) {
        if (Objects.isNull(viewName)) {
            return false;
        }
        return viewName.endsWith(extension);
    }

}
