package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KingCjy
 */
public interface HandlerInterceptor {
    void preHandle(HttpServletRequest request, HttpServletResponse response);
    void postHandler(HttpServletRequest request, HttpServletResponse response);
}
