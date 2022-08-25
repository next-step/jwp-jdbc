package core.mvc;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerInterceptorRegistry {

    private final List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public boolean applyPreHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        for (final HandlerInterceptor interceptor : interceptors) {
            final boolean preHandle = interceptor.preHandle(request, response, handler);
            if (!preHandle) {
                return false;
            }
        }

        return true;
    }

    public void applyPostHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        for (final HandlerInterceptor interceptor : interceptors) {
            interceptor.postHandle(request, response, handler);
        }
    }

    public boolean hasInterceptor() {
        return !interceptors.isEmpty();
    }
}
