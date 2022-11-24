package core.web.filter;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogMeasureInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(LogMeasureInterceptor.class);

    private Long afterTime;
    private Long beforeTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        beforeTime = System.nanoTime();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        afterTime = System.nanoTime();
        long diffTime = afterTime - beforeTime;
        logger.debug(handler.toString() + " diffTime => {}",  diffTime);
    }
}
