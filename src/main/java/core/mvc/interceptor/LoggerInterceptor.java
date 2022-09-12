package core.mvc.interceptor;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.debug("controller pre handle >>> {}", LocalDateTime.now());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.debug("controller post handle >>> {}", LocalDateTime.now());
    }
}
