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

    public void handle(Runnable service, HttpServletRequest request, HttpServletResponse response, Object handler) {
        preHandle(request, response, handler);
        service.run();
        postHandle(request, response, handler);
    }

    public void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.preHandle(request, response, handler);
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).postHandle(request, response, handler);
        }
    }
}
