package core.mvc;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerInterceptorExecutor {

    private final List<HandlerInterceptor> interceptors;

    public HandlerInterceptorExecutor(final List<HandlerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public boolean applyPreHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        for (final HandlerInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(request, response, handler)) {
                return false;
            }
        }

        return true;
    }

    public void applyPostHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        for (final HandlerInterceptor interceptor : interceptors) {
            interceptor.postHandle(request, response, handler);
        }
    }

    public void applyAfterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception exception) throws Exception {
        for (final HandlerInterceptor interceptor : interceptors) {
            interceptor.afterCompletion(request, response, handler, exception);
        }
    }

    public boolean isExecutable() {
        return !interceptors.isEmpty();
    }
}
