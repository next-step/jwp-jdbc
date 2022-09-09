package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerInterceptorImpl implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(HandlerInterceptorImpl.class);

    private final ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (stopWatchThreadLocal.get() != null) {
            stopWatchThreadLocal.get().start();
            return true;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatchThreadLocal.set(stopWatch);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        StopWatch stopWatch = stopWatchThreadLocal.get();
        if (isNotRunning(stopWatch)) {
            logger.error("StopWatch is not running! Must call preHandle method in current thread - {}",
                    Thread.currentThread());
            return;
        }
        stopWatch.stop();
        logger.debug("Thread - {}, took {} millis running process",
                Thread.currentThread(), stopWatch.getTotalTimeMillis());
    }

    private boolean isNotRunning(StopWatch stopWatch) {
        return stopWatch == null || !stopWatch.isRunning();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        stopWatchThreadLocal.remove();
    }
}
