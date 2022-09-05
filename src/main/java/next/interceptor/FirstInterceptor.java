package next.interceptor;

import core.mvc.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FirstInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(FirstInterceptor.class);

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("first preHandle");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("first postHandle");
    }
}
