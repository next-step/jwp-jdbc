package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {

    void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);

    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler);
}
