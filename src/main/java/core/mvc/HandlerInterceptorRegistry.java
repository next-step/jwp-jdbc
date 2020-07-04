package core.mvc;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerInterceptorRegistry implements HandlerInterceptor {

    private final List<HandlerInterceptor> handlerInterceptors = new ArrayList<>();

    public void addHandlerInterceptor(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptors.add(handlerInterceptor);
    }

    public void addHandlerInterceptors(List<HandlerInterceptor> handlerInterceptors) {
        this.handlerInterceptors.addAll(handlerInterceptors);
    }

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        this.handlerInterceptors.forEach(handlerInterceptor -> handlerInterceptor.preHandle(request, response, handler));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) {
        this.handlerInterceptors.forEach(handlerInterceptor -> handlerInterceptor.postHandle(request, response, handler, modelAndView));
    }

}
