package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerInterceptorExecutor {

    private List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void addInterceptor(HandlerInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public boolean applyPreHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        for (int i = 0; i < interceptors.size(); i++) {
            HandlerInterceptor interceptor = interceptors.get(i);
            if (!interceptor.preHandle(req, resp, handler)) {
                return false;
            }
        }
        return true;
    }

    public void applyPostHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView mav) {
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.postHandle(req, resp, handler, mav);
        }
    }
}
