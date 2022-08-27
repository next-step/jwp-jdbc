package core.mvc.tobe.interceptor;

import core.mvc.HandlerInterceptor;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeTraceInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TimeTraceInterceptor.class);

    private final ThreadLocal<LocalDateTime> interval = new ThreadLocal<>();

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception{
        interval.set(LocalDateTime.now());
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final LocalDateTime completeTime = LocalDateTime.now();
        logger.debug("{} 의 시작 시간: {}", handler.getClass().getName(), interval.get());
        logger.debug("{} 의 종료 시간: {}", handler.getClass().getName(), completeTime);
        logger.debug("{} 의 수행 시간: {} 밀리초", handler.getClass().getName(), ChronoUnit.MILLIS.between(interval.get(), completeTime));
        interval.remove();
    }
}
