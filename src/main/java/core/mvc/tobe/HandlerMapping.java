package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

/**
 * @author KingCjy
 */
public interface HandlerMapping {
    HandlerExecution getHandler(HttpServletRequest request);
}
