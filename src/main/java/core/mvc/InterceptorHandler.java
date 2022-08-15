package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface InterceptorHandler {
    void addInterceptor(InterceptorMetaData registry);

    Interceptors findByPathPattern(String uri);

    boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception;

    void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView modelAndView) throws Exception;

    void afterCompletionHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex);
}
