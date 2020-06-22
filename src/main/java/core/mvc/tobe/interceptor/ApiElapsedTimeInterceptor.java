package core.mvc.tobe.interceptor;

import core.mvc.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ApiElapsedTimeInterceptor implements HandlerInterceptor {
    private static final StopWatch stopwatch = new StopWatch();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        stopwatch.start();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        stopwatch.stop();
        log.debug("elapsedNanos - {}", stopwatch.getLastTaskTimeNanos());
    }
}
