package core.mvc.interceptor;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterceptorExecutor {

    private InterceptorRegistry interceptorRegistry;

    public InterceptorExecutor(final InterceptorRegistry interceptorRegistry) {
        this.interceptorRegistry = interceptorRegistry;
    }

    public void pre(HttpServletRequest request, HttpServletResponse response, Object object) {
        interceptorRegistry.pre(request, response, object);
    }

    public void post(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) {
        interceptorRegistry.post(request, response, object, modelAndView);
    }
}
