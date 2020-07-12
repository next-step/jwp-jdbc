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
    private Instant start;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        start = Instant.now();
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Instant end =  Instant.now();
        Duration duration = Duration.between(start, end);
        logger.debug("Controller: {}, processing time: {}ms", handler.getClass().getSimpleName(), duration.toMillis());
        super.postHandle(request, response, handler, modelAndView);
    }
}
