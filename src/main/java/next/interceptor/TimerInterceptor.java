package next.interceptor;

import core.annotation.Component;
import core.mvc.HandlerInterceptor;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TimerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TimerInterceptor.class);
    private static final ThreadLocal<StopWatch> threadLocal = new ThreadLocal();

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        StopWatch watch = StopWatch.createStarted();
        threadLocal.set(watch);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) {
        StopWatch stopWatch = threadLocal.get();

        logger.debug("time : {} milliseconds", stopWatch.getTime());
        threadLocal.remove();
    }
}
