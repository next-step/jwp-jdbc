package core.mvc.interceptor;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerInterceptors {

    private List<HandlerInterceptor> interceptors;

    public HandlerInterceptors(List<HandlerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return interceptors.stream()
                .allMatch(interceptor -> interceptor.preHandle(request, response, handler));
    }

    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.postHandle(request, response, handler, modelAndView);
        }
    }

}
