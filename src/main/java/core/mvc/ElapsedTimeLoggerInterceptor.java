package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ElapsedTimeLoggerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ElapsedTimeLoggerInterceptor.class);
    private final ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        StopWatch stopWatch = new StopWatch();
        stopWatchThreadLocal.set(stopWatch);
        stopWatch.start();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        StopWatch stopWatch = stopWatchThreadLocal.get();
        stopWatch.stop();
        logger.debug("request elapsed time is {}ms", stopWatch.getTotalTimeMillis());
        stopWatchThreadLocal.remove();
    }
}
