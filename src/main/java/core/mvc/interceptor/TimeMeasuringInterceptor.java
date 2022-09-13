package core.mvc.interceptor;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimeMeasuringInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(TimeMeasuringInterceptor.class);
    private static final String STOP_WATCH = "stopWatch";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        StopWatch stopWatch = new StopWatch();
        request.setAttribute(STOP_WATCH, stopWatch);
        stopWatch.start();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        StopWatch stopWatch = (StopWatch) request.getAttribute(STOP_WATCH);
        stopWatch.stop();
        logger.debug("Handler 메소드 실행 속도 : {}[sec]", stopWatch.getTotalTimeSeconds());
    }
}
