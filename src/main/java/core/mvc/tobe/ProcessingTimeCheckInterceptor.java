package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessingTimeCheckInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(ProcessingTimeCheckInterceptor.class);
    private final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
        long startTime = (long) request.getAttribute(START_TIME);
        long processingTime = System.currentTimeMillis() - startTime;

        logger.debug("[{}] 소요시간 : {}ms", request.getRequestURI(), processingTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception exception) {

    }

}
