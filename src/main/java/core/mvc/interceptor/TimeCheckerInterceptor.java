package core.mvc.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimeCheckerInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(TimeCheckerInterceptor.class);
    private final ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        StopWatch stopWatch = new StopWatch();
        stopWatchThreadLocal.set(stopWatch);
        stopWatch.start();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        StopWatch stopWatch = stopWatchThreadLocal.get();
        stopWatch.stop();
        log.debug("Thread - {}, took {} millis running process",
                Thread.currentThread(), stopWatch.getTotalTimeMillis());
        stopWatchThreadLocal.remove();
    }
}
