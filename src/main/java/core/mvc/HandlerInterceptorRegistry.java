package core.mvc;

import core.mvc.tobe.interceptor.HandlerInterceptor;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerInterceptorRegistry {
    private final List<HandlerInterceptor> handlerInterceptors = new ArrayList<>();

    public void addHandlerInterceptor(HandlerInterceptor handlerInterceptor) {
        handlerInterceptors.add(handlerInterceptor);
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (CollectionUtils.isEmpty(handlerInterceptors)) {
            return true;
        }

        for (HandlerInterceptor interceptor : handlerInterceptors) {
            if (!interceptor.preHandle(request, response, handler)) {
                interceptor.afterCompletion(request, response, handler, null);
                return false;
            }
        }

        return true;
    }

    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (CollectionUtils.isEmpty(handlerInterceptors)) {
            return;
        }

        for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
            handlerInterceptor.postHandle(request, response, handler, modelAndView);
        }
    }

    public void applyAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        if (CollectionUtils.isEmpty(handlerInterceptors)) {
            return;
        }

        for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
            handlerInterceptor.afterCompletion(request, response, handler, exception);
        }
    }
}
