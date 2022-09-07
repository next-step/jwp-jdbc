package core.mvc.tobe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessingTimeCheckInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(ProcessingTimeCheckInterceptor.class);
    private final String START_TIME = "startTime";

    @Override
    public void preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletRequest.setAttribute(START_TIME, System.currentTimeMillis());
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        long startTime = (long) httpServletRequest.getAttribute(START_TIME);
        long processingTime = System.currentTimeMillis() - startTime;

        logger.debug("[{}] 소요시간 : {}ms", httpServletRequest.getRequestURI(), processingTime);
    }

}
