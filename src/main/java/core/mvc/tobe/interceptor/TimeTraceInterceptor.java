package core.mvc.tobe.interceptor;

import core.mvc.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeTraceInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TimeTraceInterceptor.class);

    private long startTime;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        startTime = System.nanoTime();
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final String interval = String.format("%.3f", ((double) System.nanoTime() - startTime) / 1_000_000_000);
        logger.debug("{} 의 수행 시간: {}초", handler.getClass().getName(), interval);
    }
}
