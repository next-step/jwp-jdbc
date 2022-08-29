package next.dao.interceptor;

import core.mvc.HandlerAdapter;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;}
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class ControllerTimeCheckInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ControllerTimeCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LocalDateTime startTime = LocalDateTime.now();
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        LocalDateTime startTime = (LocalDateTime) request.getAttribute("startTime");
        LocalDateTime endTime = LocalDateTime.now();
        logger.debug("메소드 실행속도 {} millis", Duration.between(startTime, endTime));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("afterCompletion...");
    }
}
