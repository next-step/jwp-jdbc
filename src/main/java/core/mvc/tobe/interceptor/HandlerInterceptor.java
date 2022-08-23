package core.mvc.tobe.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {

    void preHandle(HttpServletRequest request, HttpServletResponse response);

    void postHandle(HttpServletRequest request, HttpServletResponse response);

    void afterCompletion(HttpServletRequest request, HttpServletResponse response);
}
