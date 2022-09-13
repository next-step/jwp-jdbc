package core.mvc.interceptor;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerInterceptorExecutor {
    private static final Logger logger = LoggerFactory.getLogger(HandlerInterceptorExecutor.class);

    private final List<HandlerInterceptor> interceptors;

    public HandlerInterceptorExecutor(List<HandlerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(request, response, handler)) {
                triggerAfterCompletion(request, response, handler, null);
                return false;
            }
        }
        return true;
    }

    public void applyPostHandler(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.postHandle(request, response, handler, modelAndView);
        }
    }

    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        for (HandlerInterceptor interceptor : interceptors) {
            try {
                interceptor.afterCompletion(request, response, handler, ex);
            } catch (Exception ex2) {
                logger.error("HandlerInterceptor 의 afterCompletion 메소드에서 예외가 발생하였습니다.", ex2);
            }
        }
    }
}
