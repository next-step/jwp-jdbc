package core.mvc.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SpeedInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(SpeedInterceptor.class);

    @Override
    public void preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletRequest.setAttribute("start", System.currentTimeMillis());
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        long startTime = (long) httpServletRequest.getAttribute("start");
        long time = System.currentTimeMillis() - startTime;
        logger.debug("Request URI : {}, 속도 : {}ms", httpServletRequest.getRequestURI(), time);
    }
}
