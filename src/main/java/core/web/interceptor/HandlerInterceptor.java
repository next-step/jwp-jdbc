package core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hyeyoom
 */
public interface HandlerInterceptor {

    /**
     * Called before handler is invoked.
     *
     * @param request request
     * @param response response
     * @param handler handler to execute after the interceptor is invoked.
     * @return true if the interceptor should be invoked.
     */
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);

    /**
     * Called after handler is invoked.
     *
     * @param request request
     * @param response response
     * @param handler handler
     */
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler);
}
