package core.web.interceptor;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;

public class LoggerProcessingTimeInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoggerProcessingTimeInterceptor.class);
    public static ThreadLocal<Instant> start = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        start.set(getNow());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Instant end = getNow();
        Duration duration = Duration.between(start.get(), end);
        logger.debug("Controller: {}, processing time: {}ms", handler.getClass().getSimpleName(), duration.toMillis());
        start.remove();
        super.postHandle(request, response, handler, modelAndView);
    }

    private Instant getNow() {
        return Instant.now();
    }
}
