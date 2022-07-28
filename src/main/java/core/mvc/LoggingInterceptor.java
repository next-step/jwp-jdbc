package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final ThreadLocal<StopWatch> stopWatchThreadLocal = ThreadLocal.withInitial(() -> new StopWatch());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        StopWatch stopWatch = stopWatchThreadLocal.get();
        stopWatch.start();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) {
        StopWatch stopWatch = stopWatchThreadLocal.get();
        stopWatch.stop();
        logger.debug("LoggingInterceptor : {}", stopWatch.getTotalTimeMillis());
    }
}
