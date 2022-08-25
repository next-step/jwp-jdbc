package next.dao.interceptor;

import core.mvc.HandlerAdapter;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerTimeCheckInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ControllerTimeCheckInterceptor.class);
    private long startTime;
    private long endTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        startTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        endTime = System.currentTimeMillis();
        logger.debug("메소드 실행속도 {} millis", endTime - startTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("afterCompletion...");
    }
}
