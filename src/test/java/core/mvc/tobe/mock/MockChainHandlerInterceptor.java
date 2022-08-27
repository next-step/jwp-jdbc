package core.mvc.tobe.mock;

import core.mvc.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockChainHandlerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(MockChainHandlerInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        logger.debug(handler.getClass().getName() + " 수행 전에 수행되는 로직입니다. 다음 단계로 진행합니다.");
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        logger.debug(handler.getClass().getName() + " 수행 이후 수행되는 로직입니다.");
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception exception) {
        logger.debug("View#render 수행 이후 수행되는 로직입니다. 다음 단계로 진행합니다.");
    }
}
