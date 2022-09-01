package core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionTimeCheckHandlerInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(ExecutionTimeCheckHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long currentTime = System.currentTimeMillis();
        request.setAttribute("startTime", currentTime);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long endTime = System.currentTimeMillis();
        long startTime = (long )request.getAttribute("startTime");
        long timeSpent = endTime - startTime;

        logger.debug("|Thread: " + Thread.currentThread().getName() + "| servletPath: " + request.getServletPath() + "| time spent (ms): " + timeSpent);
    }

}
