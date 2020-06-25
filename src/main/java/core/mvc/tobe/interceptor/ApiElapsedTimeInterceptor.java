package core.mvc.tobe.interceptor;

import core.mvc.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ApiElapsedTimeInterceptor implements HandlerInterceptor {
    private final StopWatch stopwatch = new StopWatch();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("ApiElapsedTimeInterceptor - preHandle()");
        stopwatch.start();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("ApiElapsedTimeInterceptor - postHandle()");
        stopwatch.stop();
        log.debug("elapsedNanos - {}", stopwatch.getLastTaskTimeNanos());
    }
}
