package core.mvc.tobe.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class TimeStampHandlerInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeStampHandlerInterceptor.class);
    private final ThreadLocal<StopWatch> stopWatches = new ThreadLocal<>();

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        if (stopWatches.get() != null) {
            stopWatches.get().start();
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatches.set(stopWatch);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        StopWatch stopWatch = stopWatches.get();
        if (isNotRunning(stopWatch)) {
            LOGGER.error("must call the preHandle method from the current thread({})", Thread.currentThread());
            return;
        }
        stopWatch.stop();
        LOGGER.debug("{} - process took {} millis to run", Thread.currentThread(), stopWatch.getTotalTimeMillis());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response) {
        stopWatches.remove();
    }

    private boolean isNotRunning(StopWatch stopWatch) {
        return stopWatch == null || !stopWatch.isRunning();
    }
}
