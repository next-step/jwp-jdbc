package core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunTimeControllerInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(RunTimeControllerInterceptor.class);
    private static final String START_TIME_NAME = "startTime";

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(START_TIME_NAME, System.currentTimeMillis());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        long startTime = (long) request.getAttribute(START_TIME_NAME);
        long responseTime = System.currentTimeMillis() - startTime;

        logger.debug("[{}] 실행 속도: {}ms", request.getRequestURI(), responseTime);
    }
}
