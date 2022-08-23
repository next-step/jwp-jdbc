package core.mvc.tobe.interceptor;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public final class HandlerInterceptors implements HandlerInterceptor {

    private static final List<HandlerInterceptor> DEFAULT_INTERCEPTORS = List.of(new TimeStampHandlerInterceptor());
    private final Collection<HandlerInterceptor> interceptors;

    private HandlerInterceptors(Collection<HandlerInterceptor> interceptors) {
        Assert.notNull(interceptors, "'interceptors' must not be null");
        this.interceptors = interceptors;
    }

    public static HandlerInterceptors defaults() {
        return new HandlerInterceptors(DEFAULT_INTERCEPTORS);
    }

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        forEach(interceptor -> interceptor.preHandle(request, response));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        forEach(interceptor -> interceptor.postHandle(request, response));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response) {
        forEach(interceptor -> interceptor.afterCompletion(request, response));
    }

    private void forEach(Consumer<HandlerInterceptor> consumer) {
        interceptors.forEach(consumer);
    }
}
