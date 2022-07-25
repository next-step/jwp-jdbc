package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerInterceptorRegistry {

    private List<HandlerInterceptor> handlerInterceptors = new ArrayList<>();

    void addHandlerInterceptor(HandlerInterceptor handlerInterceptor) {
        handlerInterceptors.add(handlerInterceptor);
    }

    void preHandles(HttpServletRequest request, HttpServletResponse response, Object handler) {
        handlerInterceptors.forEach(handlerInterceptor -> {
            boolean preHandle = handlerInterceptor.preHandle(request, response, handler);
            if (!preHandle) {
                throw new IllegalArgumentException("요청을 처리 하지 못했습니다.");
            }
        });
    }

    void postHandles(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) {
        handlerInterceptors.forEach(handlerInterceptor -> handlerInterceptor.postHandle(request, response, handler, mav));
    }
}
