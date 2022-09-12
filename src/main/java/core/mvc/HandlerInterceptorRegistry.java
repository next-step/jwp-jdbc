package core.mvc;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

public class HandlerInterceptorRegistry implements HandlerInterceptor {

    private static final List<HandlerInterceptor> INTERCEPTORS = List.of(new TimerInterceptor());

    private final Collection<HandlerInterceptor> interceptors;

    private HandlerInterceptorRegistry(Collection<HandlerInterceptor> interceptors) {
        Assert.notEmpty(interceptors, "");
        this.interceptors = interceptors;
    }

    public static HandlerInterceptorRegistry defaults() {
        return new HandlerInterceptorRegistry(INTERCEPTORS);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        interceptors.forEach(interceptor -> interceptor.preHandle(request, response, handler));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        interceptors.forEach(interceptor -> interceptor.postHandle(request, response, handler, modelAndView));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        interceptors.forEach(interceptor -> interceptor.afterCompletion(request, response, handler, ex));
    }
}
