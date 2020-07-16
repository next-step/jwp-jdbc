package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerInterceptorRegistry {

    private List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void addHandlerInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        return interceptors.stream()
                .map(i -> i.preHandle(request, response))
                .filter(bool -> !bool)
                .findFirst()
                .orElse(true);
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        interceptors.stream()
                .forEach(i -> i.postHandle(request, response, modelAndView));
    }
}
