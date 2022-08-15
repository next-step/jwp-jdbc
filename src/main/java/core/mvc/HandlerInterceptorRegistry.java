package core.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.web.interceptor.HandlerInterceptor;

public class HandlerInterceptorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(HandlerInterceptorRegistry.class);

    private final List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(interceptor);
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

    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.postHandle(request, response, handler, mav);
        }
    }

    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        for (HandlerInterceptor interceptor : interceptors) {
            try {
                interceptor.afterCompletion(request, response, handler, ex);
            } catch (Exception e) {
                logger.error("HandlerInterceptor.afterCompletion() 호출 중 예외가 발생했습니다.", e);
            }
        }
    }
}
