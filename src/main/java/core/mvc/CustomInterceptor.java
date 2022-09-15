package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(CustomInterceptor.class);

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("beginTimeStamp", System.currentTimeMillis());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Long beginTimeStamp = (Long) request.getAttribute("beginTimeStamp");
        long endTimeStamp = System.currentTimeMillis();
        logger.debug("[{}] {}, running time: {}ms", request.getMethod(), request.getRequestURI(), endTimeStamp - beginTimeStamp);
    }
}
