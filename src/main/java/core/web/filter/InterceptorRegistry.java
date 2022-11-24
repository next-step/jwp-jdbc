package core.web.filter;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {
    private final List<Interceptor> interceptorList = new ArrayList<>();

    public InterceptorRegistry() {
        interceptorList.add(new LogMeasureInterceptor());
    }

    public void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        interceptorList.forEach(interceptor -> interceptor.preHandle(request, response, handler));
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){
        interceptorList.forEach(interceptor -> interceptor.postHandle(request, response, handler, modelAndView));
    }
}
