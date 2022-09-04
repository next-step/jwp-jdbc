package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {
    boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response);
    void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse response);
}
