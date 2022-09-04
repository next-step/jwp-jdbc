package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerInterceptorRegistry {
    private final List<HandlerInterceptor> handlerInterceptors = new ArrayList<>();

    public void addHandlerInterceptor(HandlerInterceptor handlerInterceptor) {
        handlerInterceptors.add(handlerInterceptor);
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        return handlerInterceptors.stream()
                .allMatch(handlerInterceptor -> handlerInterceptor.preHandle(request, response));
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        handlerInterceptors.forEach(handlerInterceptor -> handlerInterceptor.postHandle(request, response));
    }
}
