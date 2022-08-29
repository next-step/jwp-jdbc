package next.dao.interceptor;

import core.mvc.ModelAndView;
import core.mvc.tobe.support.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static java.util.Arrays.asList;

public class HandlerInterceptorComposite implements HandlerInterceptor {

    private static final List<HandlerInterceptor> handlerInterceptors = asList(
            new ControllerTimeCheckInterceptor()
    );

    private HandlerInterceptorComposite(){}

    private static class InnerInstanceClazz {
        private static final HandlerInterceptorComposite instance = new HandlerInterceptorComposite();
    }

    public static HandlerInterceptorComposite getInstance() {
        return InnerInstanceClazz.instance;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return handlerInterceptors.stream()
                .anyMatch(handlerInterceptor -> handlerInterceptor.preHandle(request, response, handler));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        handlerInterceptors.stream()
                .filter(interceptor -> interceptor.preHandle(request, response, handler))
                .forEach(interceptor -> interceptor.postHandle(request, response, handler, modelAndView));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
