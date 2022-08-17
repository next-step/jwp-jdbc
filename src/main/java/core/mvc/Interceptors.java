package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Interceptors {
    private final List<Interceptor> store = new ArrayList<>();

    public Interceptors() {
    }

    public Interceptors(List<Interceptor> interceptors) {
        store.addAll(interceptors);
    }

    public void add(Interceptor interceptor) {
        store.add(interceptor);
    }

    public Stream<Interceptor> stream() {
        return store.stream();
    }

    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        boolean passed = true;
        for (int i = 0; i < store.size() && passed; i++) {
            Interceptor interceptor = store.get(i);
            passed = interceptor.preHandle(req, resp, handler);
        }

        return passed;
    }

    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView modelAndView) throws Exception {
        for (Interceptor interceptor : store) {
            interceptor.postHandle(req, resp, handler, modelAndView);
        }
    }

    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {
        for (Interceptor interceptor : store) {
            interceptor.afterCompletion(req, resp, handler, ex);
        }
    }
}
