package core.interceptor;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {
    private static final List<HandlerInterceptorAdapter> interceptors = new ArrayList<>();

    public InterceptorRegistry() {
        interceptors.add(new RunningTimeInterceptor());
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        for (HandlerInterceptorAdapter interceptor : interceptors) {
            boolean result = interceptor.preHandle(request, response, obj);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) throws Exception {
        for (HandlerInterceptorAdapter interceptor : interceptors) {
            interceptor.postHandle(request, response, obj, mav);
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) throws Exception {
        for (HandlerInterceptorAdapter interceptor : interceptors) {
            interceptor.afterCompletion(request, response, obj, e);
        }
    }
}
