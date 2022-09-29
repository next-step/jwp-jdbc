package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InterceptorHandler {

    private final List<InterceptorConfig> store = new ArrayList<>();

    InterceptorHandler() { }
    InterceptorHandler(InterceptorConfig interceptorConfig) {
        this.store.add(interceptorConfig);
    }

    public void addInterceptor(InterceptorConfig config) {
        store.add(config);
    }

    public List<Interceptor> findByPathPattern(String url) {
        return store.stream()
                .filter(config -> config.match(url))
                .flatMap(InterceptorConfig::getInterceptors)
                .collect(Collectors.toUnmodifiableList());
    }

    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        List<Interceptor> interceptors = findByPathPattern(request.getRequestURI());
        boolean passed = true;
        for (Interceptor interceptor : interceptors) {
            passed = interceptor.preHandle(request, response, handler);
        }

        return passed;
    }

    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        store.stream().flatMap(InterceptorConfig::getInterceptors)
                .forEach(interceptor -> interceptor.postHandle(request, response, handler, modelAndView));
    }

    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        store.stream().flatMap(InterceptorConfig::getInterceptors)
                .forEach(interceptor -> interceptor.afterCompletion(request, response, handler, exception));
    }
}
