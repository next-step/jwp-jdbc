package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class TimeLoggingInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(TimeLoggingInterceptor.class);
    private static final ThreadLocal<LocalDateTime> timeLogger = new ThreadLocal<>();

    public static final String THREAD_LOGIC_PROCEED_TIME_MS = "[Thread-{}] logic proceed time: {} ms";
    public static final String THREAD_START_TIME = "[Thread-{}] start time: {}";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug(THREAD_START_TIME, Thread.currentThread().getId(), LocalDateTime.now());
        timeLogger.set(LocalDateTime.now());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LocalDateTime startTime = timeLogger.get();
        Duration between = Duration.between(startTime, LocalDateTime.now());

        logger.debug(THREAD_LOGIC_PROCEED_TIME_MS, Thread.currentThread().getId(), between.toMillis());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        timeLogger.remove();
        if (Objects.nonNull(ex)) {
            logger.error(ex.getMessage());
        }
    }
}
