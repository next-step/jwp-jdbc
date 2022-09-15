package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerInterceptorRegistry {
    List<HandlerInterceptor> handlerInterceptors = new ArrayList<>();

    public void addInterceptor(HandlerInterceptor handlerInterceptor) {
        handlerInterceptors.add(handlerInterceptor);
    }

    void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        handlerInterceptors.forEach(handlerInterceptor -> handlerInterceptor.preHandle(request, response, handler));
    }

    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        handlerInterceptors.forEach(handlerInterceptor -> handlerInterceptor.postHandle(request, response, handler));
    }
}
