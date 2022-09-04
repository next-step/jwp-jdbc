package next.interceptor;

import core.mvc.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponseTimeInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(ResponseTimeInterceptor.class);
    private static final String START_TIME_NAME = "startTime";

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(START_TIME_NAME, System.currentTimeMillis());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        long startTime = (long) request.getAttribute("startTime");
        final long responseTime = System.currentTimeMillis() - startTime;
        logger.debug("[{}] 실행 속도: {}ms", request.getRequestURI(), responseTime);
    }
}
