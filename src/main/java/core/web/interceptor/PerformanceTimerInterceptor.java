package core.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PerformanceTimerInterceptor extends AbstractHandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PerformanceTimerInterceptor.class);

    private final ThreadLocal<Long> time = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final long t1 = System.currentTimeMillis();
        log.debug("Start time: {}", t1);
        time.set(t1);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final Long t1 = time.get();
        final long time = System.currentTimeMillis() - t1;
        log.debug("handler {} - {} ms", handler.getClass().getCanonicalName(), time);
        super.postHandle(request, response, handler);
    }
}
