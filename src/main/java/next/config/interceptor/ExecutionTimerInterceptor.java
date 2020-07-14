package next.config.interceptor;

import core.mvc.ModelAndView;
import core.mvc.interceptor.HandlerInterceptor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class ExecutionTimerInterceptor implements HandlerInterceptor {

    private static final String TIMER_START_HEADER = "preHandleStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        session.setAttribute(TIMER_START_HEADER, System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
        long postHandleEndTime = System.currentTimeMillis();

        HttpSession session = request.getSession();
        long preHandleStartTime = (long) session.getAttribute(TIMER_START_HEADER);

        log.debug("controller execution time : {} milliseconds", postHandleEndTime - preHandleStartTime);
    }

}
