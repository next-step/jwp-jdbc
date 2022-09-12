package core.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Supplier;

import core.mvc.ModelAndView;

public class InterceptorExecutor {

    private final InterceptorRegistry interceptorRegistry;

    public InterceptorExecutor(InterceptorRegistry interceptorRegistry) {
        this.interceptorRegistry = interceptorRegistry;
    }

    public ModelAndView handle(Supplier<ModelAndView> service, HttpServletRequest request, HttpServletResponse response, Object handler) {
        preHandle(request, response, handler);
        var modelAndView = service.get();
        postHandle(request, response, handler);

        return modelAndView;
    }

    private void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        for (HandlerInterceptor interceptor : interceptorRegistry.get()) {
            interceptor.preHandle(request, response, handler);
        }
    }

    private void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        for (HandlerInterceptor interceptor : interceptorRegistry.getReverseOrder()) {
            interceptor.postHandle(request, response, handler);
        }
    }
}
