package core.interceptor;

import core.mvc.HandlerInterceptor;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimeCheckInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TimeCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("interceptor preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.debug("interceptor postHandle");
    }
}
