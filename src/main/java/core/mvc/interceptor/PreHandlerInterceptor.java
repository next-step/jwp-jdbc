package core.mvc.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PreHandlerInterceptor extends InterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(PreHandlerInterceptor.class);

    @Override
    public boolean pre(final HttpServletRequest request, final HttpServletResponse response, final Object object) {
        logger.debug("this is pre handler interceptor");
        return true;
    }
}
