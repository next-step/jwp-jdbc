package core.web.interceptor;

import core.mvc.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterceptorRegistry {

    private final List<InterceptorRegistration> registrations = new ArrayList<>();

    public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor) {
        InterceptorRegistration registration = new InterceptorRegistration(interceptor);
        this.registrations.add(registration);
        return registration;
    }

    public List<HandlerInterceptor> getInterceptors() {
        return this.registrations.stream()
            .map(InterceptorRegistration::getInterceptor)
            .collect(Collectors.toList());
    }

    public void applyPreHandler(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        registrations.stream()
            .filter(interceptorRegistration -> interceptorRegistration.matches(req.getServletPath()))
            .map(InterceptorRegistration::getInterceptor)
            .forEach(interceptor -> interceptor.preHandle(req, resp, handler));
    }

    public void applyPostHandler(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView mv) {
        registrations.stream()
            .filter(interceptorRegistration -> interceptorRegistration.matches(req.getServletPath()))
            .map(InterceptorRegistration::getInterceptor)
            .forEach(interceptor -> interceptor.postHandle(req, resp, handler, mv));
    }


    public void triggerAfterCompletion(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        registrations.stream()
            .filter(interceptorRegistration -> interceptorRegistration.matches(req.getServletPath()))
            .map(InterceptorRegistration::getInterceptor)
            .forEach(interceptor -> interceptor.afterCompletion(req, resp, handler));
    }

}
