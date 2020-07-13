package core.mvc.interceptor;

import core.mvc.ModelAndView;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {

    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);

    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView);

    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception exception);

}
