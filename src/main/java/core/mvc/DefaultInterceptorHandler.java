package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultInterceptorHandler implements InterceptorHandler {
    private final List<InterceptorMetaData> store = new ArrayList<>();

    @Override
    public void addInterceptor(InterceptorMetaData registry) {
        store.add(registry);
    }

    @Override
    public Interceptors findByPathPattern(String uri) {
        List<Interceptor> result = store.stream()
                .filter(imd -> imd.matches(uri))
                .flatMap(InterceptorMetaData::stream)
                .collect(Collectors.toList());

        return new Interceptors(result);
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        Interceptors interceptors = findByPathPattern(req.getRequestURI());
        return interceptors.preHandle(req, resp, handler);
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView modelAndView) throws Exception {
        Interceptors interceptors = findByPathPattern(req.getRequestURI());
        interceptors.postHandle(req, resp, handler, modelAndView);
    }

    @Override
    public void afterCompletionHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {
        Interceptors interceptors = findByPathPattern(req.getRequestURI());
        interceptors.afterCompletion(req, resp, handler, ex);
    }
}
