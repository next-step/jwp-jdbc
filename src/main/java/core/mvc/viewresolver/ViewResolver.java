package core.mvc.viewresolver;

import core.mvc.View;

public interface ViewResolver {

    boolean supports(String viewName);

    View resolve(String viewName);

}
