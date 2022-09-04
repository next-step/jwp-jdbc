package next.interceptor;

import core.mvc.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecondInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(SecondInterceptor.class);

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("second preHandle");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("second postHandle");
    }
}
