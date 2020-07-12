package core.web.interceptor;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {
    private final List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public boolean applyPreHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(req, resp, handler)) {
                return false;
            }
        }
        return true;
    }

    public void applyPostHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView mav) throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.postHandle(req, resp, handler, mav);
        }
    }
}
