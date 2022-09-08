package core.mvc.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterceptorRegistry {

    private final List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public void handle(Runnable runnable, HttpServletRequest request, HttpServletResponse response, Object handler) {
        preHandle(request, response, handler);
        runnable.run();
        postHandle(request, response, handler);
    }

    public void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.preHandle(request, response, handler);
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.postHandle(request, response, handler);
        }
    }
}
