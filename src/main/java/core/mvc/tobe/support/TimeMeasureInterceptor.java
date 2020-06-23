package core.mvc.tobe.support;

import core.mvc.tobe.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KingCjy
 */
public class TimeMeasureInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TimeMeasureInterceptor.class);

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        startTime.set(System.currentTimeMillis());
    }

    @Override
    public void postHandler(HttpServletRequest request, HttpServletResponse response) {
        Long now = System.currentTimeMillis();
        Long start = startTime.get();

        logger.debug("Handler handle time : {} ms", now - start);
    }
}
